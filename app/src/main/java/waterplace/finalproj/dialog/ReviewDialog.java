package waterplace.finalproj.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import waterplace.finalproj.R;

public class ReviewDialog extends DialogFragment {

    private RatingBar ratingBar;
    private double rating;
    private ReviewDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_review, null);

        ratingBar = view.findViewById(R.id.ratingBar);
        Button confirmReview = view.findViewById(R.id.sendReview);
        confirmReview.setOnClickListener(v -> sendReview());

        builder.setView(view);

        return builder.create();
    }

    public interface ReviewDialogListener {
        void onRatingSelected(double rating);
    }

    public void setReviewDialogListener(ReviewDialogListener listener) {
        this.listener = listener;
    }


    private void sendReview() {
        rating = ratingBar.getRating();

        if (rating > 0) {
            if (listener != null) {
                listener.onRatingSelected(rating);
            }

            dismiss();
        } else {
            Toast.makeText(getActivity(), "Avaliação inválida. Selecione pelo menos uma estrela.", Toast.LENGTH_SHORT).show();
        }
    }

}
