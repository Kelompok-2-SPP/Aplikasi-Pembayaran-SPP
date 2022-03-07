package com.lleans.spp_kelompok_2.ui.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class MoneyTextWatcher implements TextWatcher {
    public static final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private final WeakReference<EditText> editTextWeakReference;
    private final Long maxValue;

    public MoneyTextWatcher(EditText editText, Long maxValue) {
        editTextWeakReference = new WeakReference<>(editText);
        this.maxValue = maxValue;
        numberFormat.setMaximumFractionDigits(0);
        numberFormat.setRoundingMode(RoundingMode.FLOOR);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        EditText editText = editTextWeakReference.get();
        if (editText == null || editText.getText().toString().equals("")) {
            return;
        }
        editText.removeTextChangedListener(this);

        BigDecimal parsed = parseCurrencyValue(editText.getText().toString());
        BigDecimal max = parseCurrencyValue(String.valueOf(maxValue));
        if (parsed.compareTo(max) > 0) {
            String formatted = numberFormat.format(max);
            editText.setText(formatted);
            editText.setSelection(formatted.length());
        } else {
            String formatted = numberFormat.format(parsed);
            editText.setText(formatted);
            editText.setSelection(formatted.length());
        }

        editText.addTextChangedListener(this);
    }

    public static BigDecimal parseCurrencyValue(String value) {
        try {
            String replaceRegex = String.format("[%s,.\\s]", Objects.requireNonNull(numberFormat.getCurrency()).getDisplayName());
            String currencyValue = value.replaceAll(replaceRegex, "");
            return new BigDecimal(currencyValue);
        } catch (Exception e) {
            Log.e("MyApp", e.getMessage(), e);
        }
        return BigDecimal.ZERO;
    }
}