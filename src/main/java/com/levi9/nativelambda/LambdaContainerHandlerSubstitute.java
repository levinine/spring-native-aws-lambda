package com.levi9.nativelambda;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

@TargetClass(com.amazonaws.serverless.proxy.internal.LambdaContainerHandler.class)
public final class LambdaContainerHandlerSubstitute {

    @Substitute
    private static void registerAfterBurner() {
        System.out.println("Registering after burner is not supported by the Graal VM yet ! ");
    }
}