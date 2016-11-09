        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
<#list localVars as type, name>
                ${type} ${name} = (${type}) intent.getExtras().get("${type}");
</#list>
                target.${methodName}(${paramLine});
            }
        };
        registerReceiver(context, receiver, "${action}",
                new String[]{${categories}});

