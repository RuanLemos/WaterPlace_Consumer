package waterplace.finalproj.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Locale;

import waterplace.finalproj.R;

public class ScheduleDeliveryDialog extends DialogFragment {
    private Button selectDate;
    private Button selectHour;
    private Button confirmButton;
    private Button cancelButton;
    private boolean confirmPressed;
    private TextView schedule;
    private ScheduleDeliveryListener listener;
    private String selectedDate;
    private String selectedTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_schedule_delivery, null);

        selectDate = view.findViewById(R.id.btn_select_date);
        selectHour = view.findViewById(R.id.btn_select_hour);
        schedule = view.findViewById(R.id.schedule_value);
        confirmButton = view.findViewById(R.id.btn_confirm_schedule);
        cancelButton = view.findViewById(R.id.btn_cancel_schedule);

        schedule.setText("Aguardando agendamento...");

        selectDate.setOnClickListener(v -> showDatePickerDialog());
        selectHour.setOnClickListener(v -> showTimePickerDialog());

        cancelButton.setOnClickListener(v -> dismiss());
        confirmButton.setOnClickListener(v -> dismiss());


        builder.setView(view);

        return builder.create();
    }

    public void setScheduleDeliveryListener(ScheduleDeliveryListener listener) {
        this.listener = listener;
    }

    public interface ScheduleDeliveryListener {
        void onScheduleConfirmed(String schedule);
    }

    public static class DatePickerFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            View datePickerView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_schedule_date, null);

            DatePicker datePicker = datePickerView.findViewById(R.id.date_picker);
            Button confirmButton = datePickerView.findViewById(R.id.btn_confirm_date);
            confirmButton.setOnClickListener(v -> {
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();

                String date = day + "/0" + month + "/" + year;

                if (getTargetFragment() instanceof ScheduleDeliveryDialog) {
                    ScheduleDeliveryDialog dialog = (ScheduleDeliveryDialog) getTargetFragment();
                    dialog.updateDate(date);
                }

                dismiss();
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(datePickerView);
            return builder.create();
        }
    }

    public static class TimePickerFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            View timePickerView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_schedule_time, null);

            TimePicker timePicker = timePickerView.findViewById(R.id.time_picker);
            Button confirmButton = timePickerView.findViewById(R.id.btn_confirm_time);
            confirmButton.setOnClickListener(v -> {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

                if (getTargetFragment() instanceof ScheduleDeliveryDialog) {
                    ScheduleDeliveryDialog dialog = (ScheduleDeliveryDialog) getTargetFragment();
                    dialog.updateTime(time);
                }

                dismiss();
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(timePickerView);
            return builder.create();
        }
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setTargetFragment(this, 0);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setTargetFragment(this, 0);
        timePickerFragment.show(getFragmentManager(), "timePicker");
    }

    private void updateDate(String date) {
        selectedDate = date;
        updateSchedule();
    }

    private void updateTime(String time) {
        selectedTime = time;
        updateSchedule();
    }

    private void updateSchedule() {
        if (selectedDate != null && selectedTime != null) {
            String scheduleText = "Agendado para: " + selectedDate + " às " + selectedTime;
            schedule.setText(scheduleText);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        selectedDate = null;
        selectedTime = null;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null && selectedDate != null && selectedTime != null) {
            String scheduleText = (String) schedule.getText();
            listener.onScheduleConfirmed(scheduleText);
        }
    }
}
