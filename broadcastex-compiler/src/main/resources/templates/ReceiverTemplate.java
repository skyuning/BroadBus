<#assign categoryArray = categories?join('", "')>
<#assign paramTypeArray = paramTypes?join('", "')>
        registerReceiver(
                context,
                "${action}",
                new String[]{"${categoryArray}"},
                target,
                "${methodName}",
                new String[]{"${paramTypeArray}"});

