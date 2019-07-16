package info.nightscout.androidaps.plugins.general.automation.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import info.nightscout.androidaps.R;
import info.nightscout.androidaps.plugins.general.automation.AutomationPlugin;
import info.nightscout.androidaps.plugins.general.automation.actions.Action;

public class ChooseActionDialog extends DialogFragment {

    public interface OnClickListener {
        void onClick(Action newActionObject);
    }

    private static OnClickListener mClickListener = null;

    private Unbinder mUnbinder;

    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;

    public static ChooseActionDialog newInstance() {
        Bundle args = new Bundle();

        ChooseActionDialog fragment = new ChooseActionDialog();
        fragment.setArguments(args);

        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.automation_dialog_choose_action, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        for (Action a : AutomationPlugin.actionDummyObjects) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(a.friendlyName());
            radioButton.setTag(a);
            mRadioGroup.addView(radioButton);
        }

        // restore checked radio button
        int checkedIndex = 0;
        if (savedInstanceState != null) {
            checkedIndex = savedInstanceState.getInt("checkedIndex");
        }

        ((RadioButton) mRadioGroup.getChildAt(checkedIndex)).setChecked(true);

        return view;
    }

    private int getCheckedIndex() {
        for (int i = 0; i < mRadioGroup.getChildCount(); ++i) {
            if (((RadioButton) mRadioGroup.getChildAt(i)).isChecked())
                return i;
        }
        return -1;
    }

    private Class getActionClass() {
        int radioButtonID = mRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = mRadioGroup.findViewById(radioButtonID);
        if (radioButton != null) {
            Object tag = radioButton.getTag();
            if (tag instanceof Action)
                return tag.getClass();
        }
        return null;
    }

    private Action instantiateAction() {
        Class actionClass = getActionClass();
        if (actionClass != null) {
            try {
                return (Action) actionClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    @OnClick(R.id.ok)
    @SuppressWarnings("unused")
    public void onButtonOk(View view) {
        if (mClickListener != null)
            mClickListener.onClick(instantiateAction());

        dismiss();
    }

    @OnClick(R.id.cancel)
    @SuppressWarnings("unused")
    public void onButtonCancel(View view) {
        dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt("checkedIndex", getCheckedIndex());
    }
}
