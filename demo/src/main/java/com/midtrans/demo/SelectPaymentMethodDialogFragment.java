package com.midtrans.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.midtrans.sdk.ui.models.PaymentMethodModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakawm on 5/2/17.
 */

public class SelectPaymentMethodDialogFragment extends DialogFragment {

    private static final String ARG_COLOR = "arg.color";
    private static final String ARG_LISTENER = "arg.listener";
    private static final String ARG_ENABLED_PAYMENTS = "arg.enabled.payments";

    private RecyclerView containerPaymentMethods;
    private Button okButton;
    private Button cancelButton;
    private SelectPaymentMethodAdapter adapter;

    private int color;
    private SelectPaymentMethodListener listener;

    public static SelectPaymentMethodDialogFragment newInstance(int color, SelectPaymentMethodListener listener) {
        SelectPaymentMethodDialogFragment fragment = new SelectPaymentMethodDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_COLOR, color);
        bundle.putSerializable(ARG_LISTENER, listener);
        fragment.setArguments(bundle);

        return fragment;
    }

    public static SelectPaymentMethodDialogFragment newInstance(int color,
                                                                List<SelectPaymentMethodViewModel> enabledPayments,
                                                                SelectPaymentMethodListener listener) {
        SelectPaymentMethodDialogFragment fragment = new SelectPaymentMethodDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_COLOR, color);
        bundle.putSerializable(ARG_LISTENER, listener);
        bundle.putSerializable(ARG_ENABLED_PAYMENTS, new SelectPaymentMethodListViewModel(enabledPayments));

        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_methods, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initPaymentMethodsContainer();
        initAdapter();
        initExtra();
        initThemes();
        initButtons();
    }

    private void initViews(View view) {
        containerPaymentMethods = (RecyclerView) view.findViewById(R.id.container_payment_methods);
        okButton = (Button) view.findViewById(R.id.ok_button);
        cancelButton = (Button) view.findViewById(R.id.cancel_button);
    }

    private void initPaymentMethodsContainer() {
        containerPaymentMethods.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initAdapter() {
        adapter = new SelectPaymentMethodAdapter();
        containerPaymentMethods.setAdapter(adapter);
    }

    private void initExtra() {
        color = getArguments().getInt(ARG_COLOR);
        if (color != 0) {
            adapter.setColor(color);
        }

        SelectPaymentMethodListViewModel listViewModel = (SelectPaymentMethodListViewModel) getArguments().getSerializable(ARG_ENABLED_PAYMENTS);
        if (listViewModel != null
                && listViewModel.getEnabledPayments() != null
                && !listViewModel.getEnabledPayments().isEmpty()) {
            setAdapterData(listViewModel.getEnabledPayments());
        } else {
            setAdapterData();
        }

        listener = (SelectPaymentMethodListener) getArguments().getSerializable(ARG_LISTENER);
    }

    private void setAdapterData() {
        List<PaymentMethodModel> enabledPayments = PaymentMethodUtil.getDefaultPaymentMethods(getContext());
        adapter.setPaymentMethodViewModels(mapPaymentMethods(enabledPayments));
    }

    private void setAdapterData(List<SelectPaymentMethodViewModel> enabledPayments) {
        List<SelectPaymentMethodViewModel> defaultPaymentMethods = convertPaymentMethods(PaymentMethodUtil.getDefaultPaymentMethods(getContext()));

        adapter.setPaymentMethodViewModels(filterPaymentMethods(defaultPaymentMethods, enabledPayments));
    }

    private List<SelectPaymentMethodViewModel> convertPaymentMethods(List<PaymentMethodModel> enabledPayments) {
        List<SelectPaymentMethodViewModel> viewModels = new ArrayList<>();
        for (PaymentMethodModel model : enabledPayments) {
            viewModels.add(new SelectPaymentMethodViewModel(model.getName(), model.getPaymentType(), false));
        }
        return viewModels;
    }

    private List<SelectPaymentMethodViewModel> mapPaymentMethods(List<PaymentMethodModel> enabledPayments) {
        List<SelectPaymentMethodViewModel> viewModels = new ArrayList<>();
        for (PaymentMethodModel model : enabledPayments) {
            viewModels.add(new SelectPaymentMethodViewModel(model.getName(), model.getPaymentType(), true));
        }
        return viewModels;
    }

    private List<SelectPaymentMethodViewModel> filterPaymentMethods(List<SelectPaymentMethodViewModel> defaultMethods, List<SelectPaymentMethodViewModel> enabledPayments) {
        for (SelectPaymentMethodViewModel model : defaultMethods) {
            for (SelectPaymentMethodViewModel enabledPayment : enabledPayments) {
                if (model.getMethodType().equals(enabledPayment.getMethodType())) {
                    model.setSelected(true);
                }
            }
        }
        return defaultMethods;
    }


    private void initButtons() {
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (listener != null) {
                    if (adapter.isAllPaymentMethodsSelected()) {
                        listener.onSelectAll(adapter.getSelectedPaymentMethods());
                    } else {
                        listener.onSelectPaymentMethod(adapter.getSelectedPaymentMethods());
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (listener != null) {
                    listener.onCanceled();
                }
            }
        });
    }

    private void initThemes() {
        if (color != 0) {
            okButton.setTextColor(color);
            cancelButton.setTextColor(color);
        }
    }
}
