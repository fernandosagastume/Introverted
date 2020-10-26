package com.introverted;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.appyvet.materialrangebar.RangeBar;
import com.introverted.R;

import static android.content.Context.MODE_PRIVATE;

public class MatchPreferencesDialog extends AppCompatDialogFragment {

    private Button matchMan, matchWoman, matchBoth;
    private RangeBar ageRange, distRange;
    private TextView ageMin, ageMax, distMax;
    private AlertDialog alert;
    private MatchPreferenceDialogListener listener;
    private boolean manPreference = false;
    private boolean womanPreference = false;
    private boolean bothPreference = true;
    private SharedPreferences adminSession;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        adminSession = this.getActivity().getSharedPreferences("MATCH_PREFERENCES", MODE_PRIVATE);
        TextView title = new TextView(getContext());
        // You Can Customise your Title here
        title.setText("Selecciona tus preferencias");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(22);
        title.setTypeface(ResourcesCompat.getFont(getContext(),R.font.rubik_bold));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.updatematch_settings_dialog, null);

        matchMan = view.findViewById(R.id.matchMan);
        matchWoman = view.findViewById(R.id.matchWoman);
        matchBoth = view.findViewById(R.id.matchBoth);
        ageRange = view.findViewById(R.id.ageRange);
        distRange = view.findViewById(R.id.distanceRange);
        ageMax = view.findViewById(R.id.maxRange);
        ageMin = view.findViewById(R.id.minRange);
        distMax = view.findViewById(R.id.distMax);

        int min_age  = adminSession.getInt("MATCH_MIN_AGE", 0);
        int max_age  = adminSession.getInt("MATCH_MAX_AGE", 0);
        int max_dist  = adminSession.getInt("MATCH_MAX_DIST", 0);
        String gender_preference  = adminSession.getString("MATCH_GENDER_PREFERENCE", "null");

        if(min_age > 0 && max_age > 0){
            ageRange.setRangePinsByValue(min_age, max_age);
            ageMin.setText(String.valueOf(min_age));
            ageMax.setText(String.valueOf(max_age));
        }
        if(max_dist > 0){
            if(max_dist != 201) {
                distRange.setRangePinsByValue(2, max_dist);
                view.findViewById(R.id.distMin).setVisibility(View.VISIBLE);
                view.findViewById(R.id.distMax).setVisibility(View.VISIBLE);
                view.findViewById(R.id.absoluteDis).setVisibility(View.GONE);
                distMax.setText(String.valueOf(max_dist));
            }
        }else{
            if(!distRange.getRightPinValue().equals("201")){
                view.findViewById(R.id.distMin).setVisibility(View.VISIBLE);
                view.findViewById(R.id.distMax).setVisibility(View.VISIBLE);
                view.findViewById(R.id.absoluteDis).setVisibility(View.GONE);
            }
        }

        switch (gender_preference) {
            case "Ambos":
                bothPreference = true;
                manPreference = false;
                womanPreference = false;
                break;
            case "Masculino":
                manPreference = true;
                bothPreference = false;
                womanPreference = false;
                break;
            case "Femenino":
                womanPreference = true;
                bothPreference = false;
                manPreference = false;
                break;
        }

        if(bothPreference){
            matchBoth.setTextColor(Color.parseColor("#39C34A"));
            matchBoth.setBackgroundResource(R.drawable.selected_gender_button);
        }else if(manPreference){
            matchMan.setTextColor(Color.parseColor("#39C34A"));
            matchMan.setBackgroundResource(R.drawable.selected_gender_button);
        }else if(womanPreference){
            matchWoman.setTextColor(Color.parseColor("#39C34A"));
            matchWoman.setBackgroundResource(R.drawable.selected_gender_button);
        }

        builder.setView(view).setCustomTitle(title)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = adminSession.edit();
                editor.putInt("MATCH_MIN_AGE", Integer.parseInt(ageMin.getText().toString()));
                editor.putInt("MATCH_MAX_AGE", Integer.parseInt(ageMax.getText().toString()));
                if(distMax.getVisibility() == View.VISIBLE)
                    editor.putInt("MATCH_MAX_DIST", Integer.parseInt(distMax.getText().toString()));
                else
                    editor.putInt("MATCH_MAX_DIST", 201);

                if(bothPreference)
                    editor.putString("MATCH_GENDER_PREFERENCE", "Ambos");
                else if(manPreference)
                    editor.putString("MATCH_GENDER_PREFERENCE", "Masculino");
                else if(womanPreference)
                    editor.putString("MATCH_GENDER_PREFERENCE", "Femenino");

                editor.commit();
                listener.applyChanges();
            }
        });

        matchBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bothPreference = true;
                manPreference = false;
                womanPreference = false;
                matchBoth.setTextColor(Color.parseColor("#39C34A"));
                matchBoth.setBackgroundResource(R.drawable.selected_gender_button);
                matchMan.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                matchMan.setBackgroundResource(R.drawable.button_little_border);
                matchWoman.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                matchWoman.setBackgroundResource(R.drawable.button_little_border);
            }
        });

        matchMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bothPreference = false;
                manPreference = true;
                womanPreference = false;
                matchMan.setTextColor(Color.parseColor("#39C34A"));
                matchMan.setBackgroundResource(R.drawable.selected_gender_button);
                matchBoth.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                matchBoth.setBackgroundResource(R.drawable.button_little_border);
                matchWoman.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                matchWoman.setBackgroundResource(R.drawable.button_little_border);
            }
        });

        matchWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bothPreference = false;
                manPreference = false;
                womanPreference = true;
                matchWoman.setTextColor(Color.parseColor("#39C34A"));
                matchWoman.setBackgroundResource(R.drawable.selected_gender_button);
                matchMan.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                matchMan.setBackgroundResource(R.drawable.button_little_border);
                matchBoth.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                matchBoth.setBackgroundResource(R.drawable.button_little_border);
            }
        });

        ageRange.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                ageMin.setText(String.valueOf(leftPinValue));
                ageMax.setText(String.valueOf(rightPinValue));
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {

            }
        });

        distRange.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                if(rightPinValue.equals("201")){
                    view.findViewById(R.id.distMin).setVisibility(View.GONE);
                    view.findViewById(R.id.distMax).setVisibility(View.GONE);
                    view.findViewById(R.id.absoluteDis).setVisibility(View.VISIBLE);
                }
                else {
                    view.findViewById(R.id.distMin).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.distMax).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.absoluteDis).setVisibility(View.GONE);
                    distMax.setText(rightPinValue);
                }
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {

            }
        });

        alert = builder.create();
        return alert;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (MatchPreferenceDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " debe implementar MatchPreferenceDialogListener");
        }
    }

    public interface MatchPreferenceDialogListener{
        void applyChanges();
    }

}
