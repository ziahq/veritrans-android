package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midtrans.sdk.uikit.R;


/**
 * Displays instructions related to permata bank transfer.
 * <p/>
 * Created by shivam on 10/28/15.
 */

/**
 * Displays ATM bersama payment instruction.
 * @author rakawm
 * Deprecated, use {@link com.midtrans.sdk.uikit.views.banktransfer.instruction.InstructionPermataVaFragment} instead
 */
@Deprecated
public class InstructionPermataFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_instruction_permata, container, false);
        return view;
    }
}
