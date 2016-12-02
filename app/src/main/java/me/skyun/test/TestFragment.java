package me.skyun.test;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import me.skyun.broadcastex.api.BroadBus;
import me.skyun.broadcastex.api.BroadBusReceiver;

/**
 * Created by linyun on 16/12/2.
 */

public class TestFragment extends DialogFragment {

    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadBus.getInstance().registerTarget(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BroadBus.getInstance().unregisterTarget(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, null);
        mTextView = (TextView) view.findViewById(R.id.text);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BroadBus.postFragmentViewCreated();
    }

    @BroadBusReceiver(actionTypes = Detail.class, isFragmentRefresher = true)
    public void refreshView(Context context, Detail detail) {
        Toast.makeText(context, "in refreshView: " + detail.s, Toast.LENGTH_SHORT).show();
        mTextView.setText(detail.s);
    }

    @BroadBusReceiver(actionTypes = Detail.class, isFragmentRefresher = true)
    public void refreshView2(Context context, Detail detail) {
        Toast.makeText(context, "in refreshView2: " + detail.s, Toast.LENGTH_SHORT).show();
        mTextView.setText(detail.s);
    }
}
