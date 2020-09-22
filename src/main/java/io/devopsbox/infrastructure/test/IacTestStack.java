package io.devopsbox.infrastructure.test;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;

public class IacTestStack extends Stack {
    public IacTestStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public IacTestStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);


    }
}
