package com.w3bshark.todo.signin;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Dagger Module for Sign In screen
 * We use this to pass in the View dependency to the {@link SignInPresenter}.
 */

@Module
public class SignInPresenterModule {

    private final ISignInContract.View view;

    public SignInPresenterModule(ISignInContract.View view) {
        this.view = view;
    }

    @Provides
    ISignInContract.View provideSignInContractView() {
        return view;
    }
}
