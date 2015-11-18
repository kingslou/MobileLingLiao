package com.cyt.ieasy.switcher;

import android.content.res.Resources;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cyt.ieasy.interfaces.OnErrorViewListener;

/**
 * 自定义View
 * Created by Jacek Kwiecień on 14.03.15.
 */
public class Switcher {

    private View contentView;
    private View errorView;
    private View emptyView;
    private View progressView;
    private View neterrorView;

    private TextView errorLabel;
    private TextView neterrorLabel;
    private TextView progressLabel;

    private Pair<Animations.FadeInListener, Animations.FadeOutListener> currentAnimators;

    private Switcher() {

    }


    private void setContentView(View contentView) {
        this.contentView = contentView;
    }

    private void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    private void setNeterrorView(View neterrorView){
        this.neterrorView = neterrorView;
    }

    private void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    private void setProgressView(View progressView) {
        this.progressView = progressView;
    }

    private void setErrorLabel(TextView errorLabel) {
        this.errorLabel = errorLabel;
    }

    private void setProgressLabel(TextView progressLabel) {
        this.progressLabel = progressLabel;
    }

    public static class Builder {

        private Switcher switcher = new Switcher();

        public Builder withContentView(View contentView) {
            switcher.setContentView(contentView);
            return this;
        }

        public Builder withErrorView(View errorView) {
            switcher.setErrorView(errorView);
            return this;
        }

        public Builder withNetErrorView(View netErrorView){
            switcher.setNeterrorView(netErrorView);
            return this;
        }

        public Builder withEmptyView(View emptyView) {
            switcher.setEmptyView(emptyView);
            return this;
        }

        public Builder withProgressView(View progressView) {
            switcher.setProgressView(progressView);
            return this;
        }


        public Builder withProgressLabel(TextView progressLabel) {
            switcher.setProgressLabel(progressLabel);
            return this;
        }

        public Builder withErrorLabel(TextView errorLabel) {
            switcher.setErrorLabel(errorLabel);
            return this;
        }

        public Switcher build() {
            switcher.setupViews();
            return switcher;
        }
    }

    private void setupViews() {
        if (contentView != null) contentView.setVisibility(View.VISIBLE);
        if (errorView != null) errorView.setVisibility(View.INVISIBLE);
        if(neterrorView!=null) neterrorView.setVisibility(View.INVISIBLE);
        if (emptyView != null) emptyView.setVisibility(View.INVISIBLE);
        if (progressView != null) progressView.setVisibility(View.INVISIBLE);
    }

    private static View getCurrentlyVisibleView(View viewToShow) {

        ViewParent parentView = viewToShow.getParent();

        if (parentView instanceof FrameLayout) {

            FrameLayout parent = (FrameLayout) parentView;
            View visibleView = null;

            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (child.getVisibility() == View.VISIBLE) visibleView = child;
            }
            if (visibleView != null) return visibleView;
            else throw new Resources.NotFoundException("Visible view not found");
        } else {
            throw new ClassCastException("All state views (content|error|progress|blur) should have the same FrameLayout parent");
        }

    }

    public void showContentView() {
        Log.i(Switcher.class.getSimpleName(), "showContentView");
        cancelCurrentAnimators();
        View viewToHide = getCurrentlyVisibleView(contentView);

        if (contentView != viewToHide && contentView.getVisibility() != View.VISIBLE) {
            currentAnimators = Animations.crossfadeViews(viewToHide, contentView);
        }
    }

    public void showContentViewImmediately() {
        Log.i(Switcher.class.getSimpleName(), "showContentView");
        cancelCurrentAnimators();
        View viewToHide = getCurrentlyVisibleView(contentView);

        if (contentView != viewToHide && contentView.getVisibility() != View.VISIBLE) {
            contentView.setVisibility(View.VISIBLE);
            viewToHide.setVisibility(View.INVISIBLE);
        }
    }


    public void showProgressView() {
        Log.i(Switcher.class.getSimpleName(), "showProgressView");
        cancelCurrentAnimators();
        View viewToHide = getCurrentlyVisibleView(progressView);

        if (progressView != viewToHide && progressView.getVisibility() != View.VISIBLE) {
            currentAnimators = Animations.crossfadeViews(viewToHide, progressView);
        }
    }

    public void showProgressViewImmediately() {
        Log.i(Switcher.class.getSimpleName(), "showProgressView");
        cancelCurrentAnimators();
        View viewToHide = getCurrentlyVisibleView(progressView);

        if (progressView != viewToHide && progressView.getVisibility() != View.VISIBLE) {
            progressView.setVisibility(View.VISIBLE);
            viewToHide.setVisibility(View.INVISIBLE);
        }
    }

    public void showErrorView() {
        Log.i(Switcher.class.getSimpleName(), "showErrorView");
        cancelCurrentAnimators();
        View viewToHide = getCurrentlyVisibleView(errorView);

        if (errorView != viewToHide && errorView.getVisibility() != View.VISIBLE) {
            currentAnimators = Animations.crossfadeViews(viewToHide, errorView);
        }
    }

    public void showNetErrorView(){
        cancelCurrentAnimators();
        View viewToHide = getCurrentlyVisibleView(neterrorView);

        if(neterrorView!=viewToHide && neterrorView.getVisibility()!=View.VISIBLE){
            currentAnimators = Animations.crossfadeViews(viewToHide, neterrorView);
        }
    }

    public void showErrorViewImmediately() {
        Log.i(Switcher.class.getSimpleName(), "showErrorView");
        cancelCurrentAnimators();
        View viewToHide = getCurrentlyVisibleView(errorView);

        if (errorView != viewToHide && errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
            viewToHide.setVisibility(View.INVISIBLE);
        }
    }

    public void showNetErrorViewImmediately(){
        cancelCurrentAnimators();
        View viewToHide = getCurrentlyVisibleView(neterrorView);

        if(neterrorView!=viewToHide && neterrorView.getVisibility() !=View.VISIBLE){
            neterrorView.setVisibility(View.VISIBLE);
            viewToHide.setVisibility(View.INVISIBLE);
        }
    }

    public void showEmptyView() {
        Log.i(Switcher.class.getSimpleName(), "showEmptyView");
        cancelCurrentAnimators();
        View viewToHide = getCurrentlyVisibleView(emptyView);

        if (emptyView != viewToHide && emptyView.getVisibility() != View.VISIBLE) {
            currentAnimators = Animations.crossfadeViews(viewToHide, emptyView);
        }
    }

    public void showEmptyViewImmediately() {
        Log.i(Switcher.class.getSimpleName(), "showEmptyView");
        cancelCurrentAnimators();
        View viewToHide = getCurrentlyVisibleView(emptyView);

        if (emptyView != viewToHide && emptyView.getVisibility() != View.VISIBLE) {
            emptyView.setVisibility(View.VISIBLE);
            viewToHide.setVisibility(View.INVISIBLE);
        }
    }

    private void cancelCurrentAnimators() {
        if (currentAnimators == null) return;

        Animations.FadeInListener fadeInListener = currentAnimators.first;
        if (fadeInListener != null) fadeInListener.onAnimationEnd();

        Animations.FadeOutListener fadeOutListener = currentAnimators.second;
        if (fadeOutListener != null) fadeOutListener.onAnimationEnd();
    }

    public void showErrorView(String errorMessage) {
        if (errorLabel == null) {
            throw new NullPointerException("You have to build Switcher using withErrorLabel() method");
        }

        errorLabel.setText(errorMessage);
        showErrorView();
    }

    public void showNetErrorView(final OnErrorViewListener listener){
        neterrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onErrorViewClicked();
                contentView.setOnClickListener(null);
            }
        });
        showNetErrorView();
    }

    public void showErrorView(final OnErrorViewListener listener) {
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onErrorViewClicked();
                view.setOnClickListener(null);
            }
        });
        showErrorView();
    }

    public void showErrorView(String errorMessage, final OnErrorViewListener listener) {
        if (errorLabel == null) {
            throw new NullPointerException("You have to build Switcher using withErrorLabel() method");
        }

        errorLabel.setText(errorMessage);
        showErrorView(listener);
    }

    public void showNetErrorView(String neterrorMessage,final OnErrorViewListener listener){
        if(neterrorLabel ==null){
            throw new NullPointerException("You have to build Switcher using withErrorLabel() method");
        }
        neterrorLabel.setText(neterrorMessage);
        showNetErrorView(listener);
    }

    public void showProgressView(String errorMessage) {
        if (errorLabel == null) {
            throw new NullPointerException("You have to build Switcher using withProgressLabel() method");
        }

        progressLabel.setText(errorMessage);
        showProgressView();
    }

}
