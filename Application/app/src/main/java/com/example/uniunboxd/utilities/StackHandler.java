package com.example.uniunboxd.utilities;

import androidx.fragment.app.Fragment;

import java.util.Stack;

/**
 * This class is a Singleton implementation of a Stack handler for Fragment objects.
 * Singleton ensures that only one instance of this class is created throughout the application.
 * The stack operations such as push, pop, size, empty, clear are provided.
 */
public class StackHandler {
    // Static variable reference of single_instance of type Singleton
    private static StackHandler stackHandler = null;

    // Stack to hold Fragment objects
    public Stack<Fragment> stack;

    // Private constructor to prevent instantiation from other classes
    private StackHandler() {
    }

    /**
     * This method is used to get the single instance of the StackHandler.
     * If the instance is null, a new instance is created and returned.
     * Else, the existing instance is returned.
     * @return StackHandler instance
     */
    public static synchronized StackHandler getInstance() {
        if (stackHandler == null)
            stackHandler = new StackHandler();

        return stackHandler;
    }

    /**
     * This method is used to push a Fragment object onto the stack.
     * @param f Fragment object to be pushed onto the stack
     */
    public void push(Fragment f) {
        stack.push(f);
    }

    /**
     * This method is used to pop a Fragment object from the stack.
     * @return Fragment object that is popped from the stack
     */
    public Fragment pop() {
        return stack.pop();
    }

    /**
     * This method is used to get the size of the stack.
     * @return int size of the stack
     */
    public int size() {
        return stack.size();
    }

    /**
     * This method is used to check if the stack is empty.
     * @return boolean true if the stack is empty, false otherwise
     */
    public boolean empty() {
        return stack.isEmpty();
    }

    /**
     * This method is used to clear the stack.
     */
    public void clear() {
        stack.clear();
    }

    /**
     * This method is used to set the stack with a given stack.
     * @param stack Stack of Fragment objects
     */
    public void setStack(Stack<Fragment> stack) {
        this.stack = stack;
    }
}
