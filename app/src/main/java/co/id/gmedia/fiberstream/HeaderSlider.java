package co.id.gmedia.fiberstream;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;

import co.id.gmedia.coremodul.CustomModel;

public class HeaderSlider extends Presenter {
    private static final String TAG = "HeaderSlider";

    private static final int IMG_WIDTH = 720;
    private static final int IMG_HEIGHT = 480;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {

        Log.d(TAG, "onCreateViewHolder");

        ImageView imageView = new ImageView(parent.getContext());

        imageView.setFocusable(true);
        imageView.setFocusableInTouchMode(true);
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {

        CustomModel selected = (CustomModel) item;

        ImageView imageView = (ImageView) viewHolder.view;
        Log.d(TAG, "onBindViewHolder");

        imageView.getLayoutParams().height = IMG_HEIGHT;
        imageView.getLayoutParams().width = IMG_WIDTH;
        imageView.requestLayout();
        Glide.with(viewHolder.view.getContext())
                .load(selected.getItem3())
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

        Log.d(TAG, "onUnbindViewHolder");
        ImageView imageView = (ImageView) viewHolder.view;
        imageView.setImageDrawable(null);
    }
}
