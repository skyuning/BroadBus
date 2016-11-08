        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                target.${methodName}(${paramList});
            }
        };
        registerReceiver(context, receiver, ${action});

