package com.krasovsky.dima.demoproject.main.view.custom.swipe;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.krasovsky.dima.demoproject.main.R;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class SwipeView extends FrameLayout {

    private static final int DRAG_EDGE_LEFT = 0x1;
    private static final int DRAG_EDGE_RIGHT = 0x1 << 1;
    private static final int DRAG_EDGE_BOTH = 0x1 << 2;

    private static final String TAG_LEFT_VIEW = "left";
    private static final String TAG_RIGHT_VIEW = "right";

    private int mDragEdge = DRAG_EDGE_LEFT;

    private float endPointLeftView = 0f;
    private float endPointRightView = 0f;

    private float EXTRA_SIZE_FOR_SPRING = 150f;
    private float BORDER_TO_SWIPE = 20f;

    private boolean isDragging = false;
    private boolean isOpen = false;

    private float startX;
    private float startY;

    private float previousPointX;

    public SwipeView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public SwipeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.SwipeView,
                    0, 0
            );

            mDragEdge = a.getInteger(R.styleable.SwipeView_dragFromEdge, DRAG_EDGE_LEFT);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        calculateChildrens();
        close(false);
    }

    private void calculateChildrens() {
        ViewGroup parent = ((ViewGroup) getParent());
        for (int index = 0; index < parent.getChildCount(); index++) {
            final View child = parent.getChildAt(index);
            if (child.getTag() != null && child.getTag() instanceof String)
                initEndPoints(child, (String) child.getTag());
        }
    }

    private void initEndPoints(View child, String tag) {
        if (tag.equals(TAG_LEFT_VIEW)) {
            endPointLeftView = child.getRight();
        } else if (tag.equals(TAG_RIGHT_VIEW)) {
            endPointRightView = -(getRootView().getRight() - child.getLeft());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case ACTION_DOWN:
                actionDown(event);
                break;
            case ACTION_MOVE:
                if (!isDragging && isBorder(event)) {
                    if (isDragStart(event)) {
                        isDragging = true;
                        disableParentTouch(getParent());
                    } else return false;
                } else if (isDragging) actionMove(event);
                break;
            case ACTION_CANCEL:
            case ACTION_UP:
                isDragging = false;
                actionUp(event);
                break;
        }

        return true;
    }

    private void actionDown(MotionEvent event) {
        startX = event.getRawX();
        startY = event.getRawY();
        previousPointX = startX;
    }

    private boolean isBorder(MotionEvent event) {
        float absX = Math.abs(startX - event.getRawX());
        float absY = Math.abs(startY - event.getRawY());
        return absX >= BORDER_TO_SWIPE || absY >= BORDER_TO_SWIPE;
    }

    private boolean isDragStart(MotionEvent event) {
        float absX = Math.abs(startX - event.getRawX());
        float absY = Math.abs(startY - event.getRawY());
        return absX > absY;
    }

    private void actionMove(MotionEvent event) {
        float distance = -(previousPointX - event.getRawX());
        moveView(distance);
        previousPointX = event.getRawX();
    }

    private void moveView(float distance) {
        if (distance == 0) return;
        int transition = calculateTransition(distance);
        switch (mDragEdge) {
            case DRAG_EDGE_LEFT:
                if (transition < 0) {
                    transition = 0;
                }
                break;
            case DRAG_EDGE_RIGHT:
                if (transition > 0) {
                    transition = 0;
                }
                break;
            case DRAG_EDGE_BOTH:
                if (isOpen) {
                    if (getTranslationX() < 0) {
                        transition = transition > 0 ? 0 : transition;
                    } else if (getTranslationX() > 0) {
                        transition = transition < 0 ? 0 : transition;
                    } else transition = 0;
                }
                break;
        }
        setTranslationX(transition);
    }

    private int calculateTransition(float distance) {
        float currentTransitionX = getTranslationX();
        float transition;
        if (currentTransitionX + distance < endPointRightView - EXTRA_SIZE_FOR_SPRING) {
            transition = endPointRightView - EXTRA_SIZE_FOR_SPRING;
        } else if (currentTransitionX + distance > endPointLeftView + EXTRA_SIZE_FOR_SPRING) {
            transition = endPointLeftView + EXTRA_SIZE_FOR_SPRING;
        } else transition = distance + currentTransitionX;
        return (int) transition;
    }

    private void actionUp(MotionEvent event) {
        if (getTranslationX() >= endPointLeftView / 2) {
            open(true, (int) endPointLeftView);
        } else if (getTranslationX() <= endPointRightView / 2) {
            open(true, (int) endPointRightView);
        } else {
            close(true);
        }
    }

    private void open(boolean animation, int endPoint) {
        isOpen = true;
        if (animation) {
            ObjectAnimator.ofFloat(this, "translationX", getTranslationX(), endPoint)
                    .start();
        } else {
            setTranslationX(endPoint);
        }
    }

    private void close(boolean animation) {
        isOpen = false;
        if (animation) {
            ObjectAnimator.ofFloat(this, "translationX", getTranslationX(), 0)
                    .start();
        } else {
            setTranslationX(0);
        }
    }

    private void disableParentTouch(ViewParent view) {
        view.requestDisallowInterceptTouchEvent(true);
        if (view.getParent() != null) disableParentTouch((view.getParent()));
    }
}
