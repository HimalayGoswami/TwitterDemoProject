package com.TwitterDemo;

import java.security.Permission;

public class NoExitSecurityManager extends SecurityManager
{
    public static class ExitException extends SecurityException
    {
        public final int status;
        public ExitException(int status)
        {
            super("There is no escape!");
            this.status = status;
        }
    }

    @Override
    public void checkPermission(Permission perm)
    {
        // allow anything.
    }
    @Override
    public void checkPermission(Permission perm, Object context)
    {
        // allow anything.
    }
    @Override
    public void checkExit(int status)
    {
        //super.checkExit(status);
        throw new ExitException(status);
    }
}
