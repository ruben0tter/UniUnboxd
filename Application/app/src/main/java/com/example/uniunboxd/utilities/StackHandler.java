package com.example.uniunboxd.utilities;

import androidx.fragment.app.Fragment;

import java.util.Stack;

public class StackHandler {
    // Static variable reference of single_instance
    // of type Singleton
    private static StackHandler stackHandler = null;

    public Stack<Fragment> stack;

    private StackHandler() { }

    public static synchronized StackHandler getInstance()
    {
        if (stackHandler == null)
            stackHandler = new StackHandler();

        return stackHandler;
    }

    public void setStack(Stack<Fragment> stack) {
        this.stack = stack;
    }
}