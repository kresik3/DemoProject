package com.krasovsky.dima.demoproject.main.view.custom.swipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.krasovsky.dima.demoproject.main.R;


@SuppressLint("RtlHardcoded")
public class SwipeRevealLayout extends ViewGroup {

    private static final String SUPER_INSTANCE_STATE = "saved_instance_state_parcelable";

    private static final int DEFAULT_MIN_FLING_VELOCITY = 300; // dp per second
    private static final int DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT = 1; // dp

    public static final int DRAG_EDGE_LEFT = 0x1;
    public static final int DRAG_EDGE_RIGHT = 0x1 << 1;
    public static final int DRAG_EDGE_BOTH = 0x1 << 2;

    /**
     * The secondary view will be under the main view.
     */
    public static final int MODE_NORMAL = 0;

    /**
     * The secondary view will stick the edge of the main view.
     */
    public static final int MODE_SAME_LEVEL = 1;

    /**
     * Main view is the view which is shown when the layout is closed.
     */
    private View mMainView;

    /**
     * Secondary view is the view which is shown when the layout is opened.
     */
    private View mSecondaryView;

    /**
     * Third view is the view which is shown when the layout is opened left.
     */
    private View mThirdView;

    /**
     * The rectangle position of the main view when the layout is closed.
     */
    private Rect mRectMainClose = new Rect();

    /**
     * The rectangle position of the main view when the layout is opened.
     */
    private Rect mRectMainOpen = new Rect();

    /**
     * The rectangle position of the main view when the layout is opened left.
     */
    private Rect mRectMainOpenLeft = new Rect();

    /**
     * The rectangle position of the secondary view when the layout is closed.
     */
    private Rect mRectSecClose = new Rect();

    /**
     * The rectangle position of the third view when the layout is closed.
     */
    private Rect mRectThirdClose = new Rect();

    /**
     * The rectangle position of the secondary view when the layout is opened.
     */
    private Rect mRectSecOpen = new Rect();

    /**
     * The rectangle position of the third view when the layout is opened left.
     */
    private Rect mRectThirdOpen = new Rect();


    /**
     * The minimum distance (px) to the closest drag edge that the SwipeRevealLayout
     * will disallow the parent to intercept touch event.
     */
    private int mMinDistRequestDisallowParent = 0;

    private volatile boolean mIsScrolling = false;
    private volatile boolean mLockDrag = false;

    private int mMinFlingVelocity = DEFAULT_MIN_FLING_VELOCITY;
    private int mMode = MODE_NORMAL;

    private int mDragEdge = DRAG_EDGE_LEFT;

    private float mDragDist = 0;
    private float mPrevX = -1;

    private ViewDragHelper mDragHelper;
    private GestureDetectorCompat mGestureDetector;

    public SwipeRevealLayout(Context context) {
        super(context);
        init(context, null);
    }

    public SwipeRevealLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SwipeRevealLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_INSTANCE_STATE, super.onSaveInstanceState());
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        state = bundle.getParcelable(SUPER_INSTANCE_STATE);
        super.onRestoreInstanceState(state);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mDragHelper.processTouchEvent(event);
        return isInsideMain(event.getX(), event.getY());
    }

    private boolean isInsideMain(float x, float y) {
        return x >= mMainView.getLeft() && x <= mMainView.getRight()
                && y >= mMainView.getTop() && y <= mMainView.getBottom();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isDragLocked()) {
            return super.onInterceptTouchEvent(ev);
        }

        mDragHelper.processTouchEvent(ev);
        mGestureDetector.onTouchEvent(ev);
        accumulateDragDist(ev);

        boolean couldBecomeClick = couldBecomeClick(ev);
        boolean settling = mDragHelper.getViewDragState() == ViewDragHelper.STATE_SETTLING;
        boolean idleAfterScrolled = mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE
                && mIsScrolling;

        // must be placed as the last statement
        mPrevX = ev.getX();

        // return true => intercept, cannot trigger onClick event
        return !couldBecomeClick && (settling || idleAfterScrolled) && isInsideMain(ev.getX(), ev.getY());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // get views
        if (getChildCount() == 3) {
            mSecondaryView = getChildAt(0);
            mThirdView = getChildAt(1);
            mMainView = getChildAt(2);
        } else if (getChildCount() == 2) {
            mSecondaryView = getChildAt(0);
            mMainView = getChildAt(1);
        } else if (getChildCount() == 1) {
            mMainView = getChildAt(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);

            int left, right, top, bottom;
            left = right = top = bottom = 0;

            final int minLeft = getPaddingLeft();
            final int maxRight = Math.max(r - getPaddingRight() - l, 0);
            final int minTop = getPaddingTop();
            final int maxBottom = Math.max(b - getPaddingBottom() - t, 0);

            int measuredChildHeight = child.getMeasuredHeight();
            int measuredChildWidth = child.getMeasuredWidth();

            // need to take account if child size is match_parent
            final LayoutParams childParams = child.getLayoutParams();
            boolean matchParentHeight = false;
            boolean matchParentWidth = false;

            if (childParams != null) {
                matchParentHeight = (childParams.height == LayoutParams.MATCH_PARENT) ||
                        (childParams.height == LayoutParams.FILL_PARENT);
                matchParentWidth = (childParams.width == LayoutParams.MATCH_PARENT) ||
                        (childParams.width == LayoutParams.FILL_PARENT);
            }

            if (matchParentHeight) {
                measuredChildHeight = maxBottom - minTop;
                childParams.height = measuredChildHeight;
            }

            if (matchParentWidth) {
                measuredChildWidth = maxRight - minLeft;
                childParams.width = measuredChildWidth;
            }

            switch (mDragEdge) {
                case DRAG_EDGE_RIGHT:
                    left = Math.max(r - measuredChildWidth - getPaddingRight() - l, minLeft);
                    top = Math.min(getPaddingTop(), maxBottom);
                    right = Math.max(r - getPaddingRight() - l, minLeft);
                    bottom = Math.min(measuredChildHeight + getPaddingTop(), maxBottom);
                    break;
                case DRAG_EDGE_LEFT:
                    left = Math.min(getPaddingLeft(), maxRight);
                    top = Math.min(getPaddingTop(), maxBottom);
                    right = Math.min(measuredChildWidth + getPaddingLeft(), maxRight);
                    bottom = Math.min(measuredChildHeight + getPaddingTop(), maxBottom);
                    break;
                case DRAG_EDGE_BOTH:
                    String tag = getTag(child);
                    if (tag.equals("left")) {
                        left = Math.min(getPaddingLeft(), maxRight);
                        top = Math.min(getPaddingTop(), maxBottom);
                        right = Math.min(measuredChildWidth + getPaddingLeft(), maxRight);
                        bottom = Math.min(measuredChildHeight + getPaddingTop(), maxBottom);
                    } else if (tag.equals("right")) {
                        left = Math.max(r - measuredChildWidth - getPaddingRight() - l, minLeft);
                        top = Math.min(getPaddingTop(), maxBottom);
                        right = Math.max(r - getPaddingRight() - l, minLeft);
                        bottom = Math.min(measuredChildHeight + getPaddingTop(), maxBottom);
                    }
                    break;
            }

            child.layout(left, top, right, bottom);
        }

        // taking account offset when mode is SAME_LEVEL
        if (mMode == MODE_SAME_LEVEL) {
            switch (mDragEdge) {
                case DRAG_EDGE_LEFT:
                    mSecondaryView.offsetLeftAndRight(-mSecondaryView.getWidth());
                    break;
                case DRAG_EDGE_RIGHT:
                    mSecondaryView.offsetLeftAndRight(mSecondaryView.getWidth());
                    break;
                case DRAG_EDGE_BOTH:
                    mThirdView.offsetLeftAndRight(-mThirdView.getWidth());
                    mSecondaryView.offsetLeftAndRight(mSecondaryView.getWidth());
                    break;
            }
        }

        initRects();

        close(false);
    }

    private String getTag(View view) {
        Object tagObject = view.getTag();
        String tag;
        if (tagObject == null) {
            tag = "right";
        } else {
            tag = tagObject.toString();
        }
        return tag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() < 2) {
            throw new RuntimeException("Layout must have two children");
        }

        final LayoutParams params = getLayoutParams();

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int desiredWidth = 0;
        int desiredHeight = 0;

        // first find the largest child
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            desiredWidth = Math.max(child.getMeasuredWidth(), desiredWidth);
            desiredHeight = Math.max(child.getMeasuredHeight(), desiredHeight);
        }
        // create new measure spec using the largest child width
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(desiredWidth, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, heightMode);

        final int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final LayoutParams childParams = child.getLayoutParams();

            if (childParams != null) {
                if (childParams.height == LayoutParams.MATCH_PARENT) {
                    child.setMinimumHeight(measuredHeight);
                }

                if (childParams.width == LayoutParams.MATCH_PARENT) {
                    child.setMinimumWidth(measuredWidth);
                }
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            desiredWidth = Math.max(child.getMeasuredWidth(), desiredWidth);
            desiredHeight = Math.max(child.getMeasuredHeight(), desiredHeight);
        }

        // taking accounts of padding
        desiredWidth += getPaddingLeft() + getPaddingRight();
        desiredHeight += getPaddingTop() + getPaddingBottom();

        // adjust desired width
        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = measuredWidth;
        } else {
            if (params.width == LayoutParams.MATCH_PARENT) {
                desiredWidth = measuredWidth;
            }

            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = (desiredWidth > measuredWidth) ? measuredWidth : desiredWidth;
            }
        }

        // adjust desired height
        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = measuredHeight;
        } else {
            if (params.height == LayoutParams.MATCH_PARENT) {
                desiredHeight = measuredHeight;
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = (desiredHeight > measuredHeight) ? measuredHeight : desiredHeight;
            }
        }

        setMeasuredDimension(desiredWidth, desiredHeight);
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * Open the panel to show the secondary view
     */
    public void open(boolean animation, Rect rectMainOpen) {
        if (animation) {
            mDragHelper.smoothSlideViewTo(mMainView, rectMainOpen.left, rectMainOpen.top);
        } else {
            mDragHelper.abort();

            mMainView.layout(
                    rectMainOpen.left,
                    rectMainOpen.top,
                    rectMainOpen.right,
                    rectMainOpen.bottom
            );

            mSecondaryView.layout(
                    mRectSecOpen.left,
                    mRectSecOpen.top,
                    mRectSecOpen.right,
                    mRectSecOpen.bottom
            );

            mThirdView.layout(
                    mRectThirdOpen.left,
                    mRectThirdOpen.top,
                    mRectThirdOpen.right,
                    mRectThirdOpen.bottom
            );
        }

        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * Close the panel to hide the secondary view
     */
    public void close(boolean animation) {
        if (animation) {
            mDragHelper.smoothSlideViewTo(mMainView, mRectMainClose.left, mRectMainClose.top);
        } else {
            mDragHelper.abort();
            mMainView.layout(
                    mRectMainClose.left,
                    mRectMainClose.top,
                    mRectMainClose.right,
                    mRectMainClose.bottom
            );
            mSecondaryView.layout(
                    mRectSecClose.left,
                    mRectSecClose.top,
                    mRectSecClose.right,
                    mRectSecClose.bottom
            );

            if (mThirdView != null) {
                mThirdView.layout(
                        mRectThirdClose.left,
                        mRectThirdClose.top,
                        mRectThirdClose.right,
                        mRectThirdClose.bottom
                );
            }
        }

        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * @return true if the drag/swipe motion is currently locked.
     */
    public boolean isDragLocked() {
        return mLockDrag;
    }

    private int getMainOpenLeft(int dragEdger, View secondaryView) {
        switch (dragEdger) {
            case DRAG_EDGE_LEFT:
                return mRectMainClose.left + secondaryView.getWidth();
            case DRAG_EDGE_RIGHT:
                return mRectMainClose.left - secondaryView.getWidth();
            default:
                return 0;
        }
    }

    private int getMainOpenTop() {
        switch (mDragEdge) {
            case DRAG_EDGE_LEFT:
                return mRectMainClose.top;
            case DRAG_EDGE_RIGHT:
                return mRectMainClose.top;
            case DRAG_EDGE_BOTH:
                return mRectMainClose.top;
            default:
                return 0;
        }
    }

    private int getSecOpenLeft() {
        return mRectSecClose.left;
    }

    private int getSecOpenTop() {
        return mRectSecClose.top;
    }

    private int getThirdOpenLeft() {
        return mRectThirdClose.left;
    }

    private int getThirdOpenTop() {
        return mRectThirdClose.top;
    }

    private void initRects() {
        // close position of main view
        mRectMainClose.set(
                mMainView.getLeft(),
                mMainView.getTop(),
                mMainView.getRight(),
                mMainView.getBottom()
        );

        // close position of secondary view
        mRectSecClose.set(
                mSecondaryView.getLeft(),
                mSecondaryView.getTop(),
                mSecondaryView.getRight(),
                mSecondaryView.getBottom()
        );

        if (mThirdView != null) {
            // close position of third view
            mRectThirdClose.set(
                    mThirdView.getLeft(),
                    mThirdView.getTop(),
                    mThirdView.getRight(),
                    mThirdView.getBottom()
            );
        }

        // open position of the main view
        mRectMainOpen.set(
                getMainOpenLeft(DRAG_EDGE_RIGHT, mSecondaryView),
                getMainOpenTop(),
                getMainOpenLeft(DRAG_EDGE_RIGHT, mSecondaryView) + mMainView.getWidth(),
                getMainOpenTop() + mMainView.getHeight()
        );

        if (mThirdView != null) {
            // open position of the main view left
            mRectMainOpenLeft.set(
                    getMainOpenLeft(DRAG_EDGE_LEFT, mThirdView),
                    getMainOpenTop(),
                    getMainOpenLeft(DRAG_EDGE_LEFT, mThirdView) + mMainView.getWidth(),
                    getMainOpenTop() + mMainView.getHeight()
            );
        }

        // open position of the secondary view
        mRectSecOpen.set(
                getSecOpenLeft(),
                getSecOpenTop(),
                getSecOpenLeft() + mSecondaryView.getWidth(),
                getSecOpenTop() + mSecondaryView.getHeight()
        );

        if (mThirdView != null) {
            // open position of the third view
            mRectThirdOpen.set(
                    getThirdOpenLeft(),
                    getThirdOpenTop(),
                    getThirdOpenLeft() + mThirdView.getWidth(),
                    getThirdOpenTop() + mThirdView.getHeight()
            );
        }
    }

    private boolean couldBecomeClick(MotionEvent ev) {
        return isInMainView(ev) && !shouldInitiateADrag();
    }

    private boolean isInMainView(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();

        boolean withinVertical = mMainView.getTop() <= y && y <= mMainView.getBottom();
        boolean withinHorizontal = mMainView.getLeft() <= x && x <= mMainView.getRight();

        return withinVertical && withinHorizontal;
    }

    private boolean shouldInitiateADrag() {
        float minDistToInitiateDrag = mDragHelper.getTouchSlop();
        return mDragDist >= minDistToInitiateDrag;
    }

    private void accumulateDragDist(MotionEvent ev) {
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mDragDist = 0;
            return;
        }

        float dragged = Math.abs(ev.getX() - mPrevX);

        mDragDist += dragged;
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null && context != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.SwipeRevealLayout,
                    0, 0
            );

            mDragEdge = a.getInteger(R.styleable.SwipeRevealLayout_dragFromEdge, DRAG_EDGE_LEFT);
            mMode = MODE_NORMAL;
            mMinFlingVelocity = DEFAULT_MIN_FLING_VELOCITY;
            mMinDistRequestDisallowParent = DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT;
        }

        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragHelperCallback);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);

        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);
    }

    private final GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        boolean hasDisallowed = false;

        @Override
        public boolean onDown(MotionEvent e) {
            mIsScrolling = false;
            hasDisallowed = false;
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mIsScrolling = true;
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mIsScrolling = true;

            if (getParent() != null) {
                boolean shouldDisallow;

                if (!hasDisallowed) {
                    shouldDisallow = getDistToClosestEdge(distanceX) >= mMinDistRequestDisallowParent;
                    if (shouldDisallow) {
                        hasDisallowed = true;
                    }
                } else {
                    shouldDisallow = true;
                }

                // disallow parent to intercept touch event so that the layout will work
                // properly on RecyclerView or view that handles scroll gesture.
                getParent().requestDisallowInterceptTouchEvent(shouldDisallow);
            }

            return false;
        }
    };

    private int getDistToClosestEdge(float distanceX) {
        switch (mDragEdge) {
            case DRAG_EDGE_LEFT:
                final int pivotRight = mRectMainClose.left + mSecondaryView.getWidth();
                return Math.min(
                        mMainView.getLeft() - mRectMainClose.left,
                        pivotRight - mMainView.getLeft()
                );
            case DRAG_EDGE_RIGHT:
                final int pivotLeft = mRectMainClose.right - mSecondaryView.getWidth();
                return Math.min(
                        mMainView.getRight() - pivotLeft,
                        mRectMainClose.right - mMainView.getRight()
                );
            case DRAG_EDGE_BOTH:
                if (distanceX < 0) {
                    final int pivot = mRectMainClose.right - mSecondaryView.getWidth();
                    return Math.min(
                            mMainView.getRight() - pivot,
                            mRectMainClose.right - mMainView.getRight()
                    );
                } else {
                    final int pivot = mRectMainClose.left + mThirdView.getWidth();
                    return Math.min(
                            mMainView.getLeft() - mRectMainClose.left,
                            pivot - mMainView.getLeft()
                    );
                }
        }

        return 0;
    }

    private int getHalfwayPivotHorizontal(int dragEdge) {
        if (dragEdge == DRAG_EDGE_LEFT) {
            return mRectMainClose.left + mSecondaryView.getWidth() / 2;
        } else {
            return mRectMainClose.right - mSecondaryView.getWidth() / 2;
        }
    }

    private final ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {

            if (mLockDrag)
                return false;

            mDragHelper.captureChildView(mMainView, pointerId);
            return false;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            switch (mDragEdge) {
                case DRAG_EDGE_RIGHT:
                    return Math.max(
                            Math.min(left, mRectMainClose.left),
                            mRectMainClose.left - mSecondaryView.getWidth()
                    );

                case DRAG_EDGE_LEFT:
                    return Math.max(
                            Math.min(left, mRectMainClose.left + mSecondaryView.getWidth()),
                            mRectMainClose.left
                    );
                case DRAG_EDGE_BOTH:
                    if (left < 0) {
                        return Math.max(
                                Math.min(left, mRectMainClose.left),
                                mRectMainClose.left - mSecondaryView.getWidth()
                        );
                    } else {
                        return Math.max(
                                Math.min(left, mRectMainClose.left + mThirdView.getWidth()),
                                mRectMainClose.left
                        );
                    }

                default:
                    return child.getLeft();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final boolean velRightExceeded = pxToDp((int) xvel) >= mMinFlingVelocity;
            final boolean velLeftExceeded = pxToDp((int) xvel) <= -mMinFlingVelocity;

            int pivotHorizontal = 0;

            switch (mDragEdge) {
                case DRAG_EDGE_RIGHT:
                    pivotHorizontal = getHalfwayPivotHorizontal(DRAG_EDGE_RIGHT);
                    if (velRightExceeded) {
                        close(true);
                    } else if (velLeftExceeded) {
                        open(true, mRectMainOpen);
                    } else {
                        if (mMainView.getRight() < pivotHorizontal) {
                            open(true, mRectMainOpen);
                        } else {
                            close(true);
                        }
                    }
                    break;

                case DRAG_EDGE_LEFT:
                    pivotHorizontal = getHalfwayPivotHorizontal(DRAG_EDGE_LEFT);
                    if (velRightExceeded) {
                        open(true, mRectMainOpen);
                    } else if (velLeftExceeded) {
                        close(true);
                    } else {
                        if (mMainView.getLeft() < pivotHorizontal) {
                            close(true);
                        } else {
                            open(true, mRectMainOpen);
                        }
                    }
                    break;
                case DRAG_EDGE_BOTH:
                    if (releasedChild.getX() < 0) {
                        pivotHorizontal = getHalfwayPivotHorizontal(DRAG_EDGE_RIGHT);
                        if (velRightExceeded) {
                            close(true);
                        } else if (velLeftExceeded) {
                            open(true, mRectMainOpen);
                        } else {
                            if (mMainView.getRight() < pivotHorizontal) {
                                open(true, mRectMainOpen);
                            } else {
                                close(true);
                            }
                        }
                    } else {
                        pivotHorizontal = getHalfwayPivotHorizontal(DRAG_EDGE_LEFT);
                        if (velRightExceeded) {
                            open(true, mRectMainOpenLeft);
                        } else if (velLeftExceeded) {
                            close(true);
                        } else {
                            if (mMainView.getLeft() < pivotHorizontal) {
                                close(true);
                            } else {
                                open(true, mRectMainOpenLeft);
                            }
                        }
                    }
                    break;
            }
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);

            if (mLockDrag) {
                return;
            }

            boolean edgeStartLeft = (mDragEdge == DRAG_EDGE_RIGHT)
                    && edgeFlags == ViewDragHelper.EDGE_LEFT;

            boolean edgeStartRight = (mDragEdge == DRAG_EDGE_LEFT)
                    && edgeFlags == ViewDragHelper.EDGE_RIGHT;

            if (mDragEdge == DRAG_EDGE_BOTH) {
                edgeStartLeft = true;
                edgeStartRight = true;
            }

            if (edgeStartLeft || edgeStartRight) {
                mDragHelper.captureChildView(mMainView, pointerId);
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (mMode == MODE_SAME_LEVEL) {
                if (mDragEdge == DRAG_EDGE_LEFT || mDragEdge == DRAG_EDGE_RIGHT || mDragEdge == DRAG_EDGE_BOTH) {
                    mSecondaryView.offsetLeftAndRight(dx);
                } else {
                    mSecondaryView.offsetTopAndBottom(dy);
                }
            }
            ViewCompat.postInvalidateOnAnimation(SwipeRevealLayout.this);
        }
    };

    private int pxToDp(int px) {
        Resources resources = getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}