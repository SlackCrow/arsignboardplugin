package edu.buffalo.cse622.plugins;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import android.graphics.Color;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;
import java.util.HashSet;

public class FrameOperations {
    Context _context;
    ArFragment _fragment;
    Resources _resources;
    ViewRenderable _textRenderable;
    HashSet<AnchorNode> _pluginObjects;
    int _layoutId;

    public FrameOperations(Resources dynamicResources, ArFragment fragment, HashSet<AnchorNode> pluginObjects) {
        _pluginObjects = pluginObjects;
        _context = fragment.getContext();
        _fragment = fragment;
        _resources = dynamicResources;

        _layoutId = dynamicResources.getIdentifier("signboard", "layout", "edu.buffalo.cse622.plugins");
        XmlResourceParser textViewXml = dynamicResources.getLayout(_layoutId);
        View view = LayoutInflater.from(_context).inflate(textViewXml, null);


    }
    private void processFrame(Frame frame) {
    }


    private void planeTap(HitResult hitResult) {

        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle("Text to Print");
        EditText textInput = new EditText(_context);
        TextView tv = new TextView(_context);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setBackgroundColor(Color.parseColor("#ffb5d6e1"));
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                ViewRenderable.builder()
                        .setView(_context, tv)
                        .build()
                        .thenAccept(
                                (renderable) -> {
                                    _textRenderable = renderable;
                                    tv.setText(textInput.getText().toString());

                                    Anchor anchor = hitResult.createAnchor();
                                    AnchorNode anchorNode = new AnchorNode(anchor);

                                    TransformableNode transNode = new TransformableNode(_fragment.getTransformationSystem());
                                    transNode.setParent(anchorNode);
                                    transNode.setRenderable(_textRenderable);
                                    transNode.select();
                                    _pluginObjects.add(anchorNode);
                                    anchorNode.setParent(_fragment.getArSceneView().getScene());
                                });

            }
        });

        builder.setView(textInput);
        builder.show();
    }

}