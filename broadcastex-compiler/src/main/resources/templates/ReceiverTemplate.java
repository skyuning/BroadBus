BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        $target.${method}(context, intent);
    }
};
registerReceiver(context, receiver, ${action}, ${categories}) {

