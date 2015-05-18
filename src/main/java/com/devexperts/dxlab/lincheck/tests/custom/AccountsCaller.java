package com.devexperts.dxlab.lincheck.tests.custom;


import com.devexperts.dxlab.lincheck.tests.custom.transfer.Accounts;
import com.devexperts.dxlab.lincheck.util.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AccountsCaller implements Caller {
    Accounts accounts;

    Class objClass;

    public AccountsCaller(Class objClass) {
        this.objClass = objClass;
        reload();
    }

    public void reload() {
        try {
            Constructor ctor = objClass.getConstructor();
            accounts = (Accounts) ctor.newInstance();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /*
        0 - getAmount(id)
        1 - setAmount(id, value)
        2 - transfer(from, to, value)
    */
    public void call(Actor act, Result res) {
        res.setUndefined();

        int method = act.method;
        Object[] args = act.args;

        if (method == 0) {
            Integer id = (Integer) args[0];
            Integer value = accounts.getAmount(id);
            res.setValue(value);
        } else if (method == 1) {
            Integer id = (Integer) args[0];
            Integer value = (Integer) args[1];
            accounts.setAmount(id, value);
            res.setVoid();
        } else if (method == 2) {
            Integer from = (Integer) args[0];
            Integer to = (Integer) args[1];
            Integer value = (Integer) args[2];
            accounts.transfer(from, to, value);
            res.setVoid();
        }
    }

    @Override
    public List<CheckerConfiguration> getConfigurations() {
        return null; // TODO fix
    }


    public Actor[][] generateActors(int numThreads) {
        Actor[][] actors = new Actor[numThreads][];
        int ind = 0;
        for (int i = 0; i < numThreads; i++) {
            int cnt = MyRandom.nextInt(2) + 1;
            actors[i] = new Actor[cnt];
            for (int j = 0; j < cnt; j++) {
                int t = MyRandom.nextInt(3);
                if (t == 0) {
                    actors[i][j] = new Actor(ind++, 0, MyRandom.nextInt(2));
                    actors[i][j].methodName = "get";
                } else if (t == 1) {
                    actors[i][j] = new Actor(ind++, 1, MyRandom.nextInt(2), MyRandom.nextInt(10));
                    actors[i][j].methodName = "set";
                } else if (t == 2) {
                    int from = -1;
                    int to = -1;

                    while (from == to) {
                        from = MyRandom.nextInt(2);
                        to = MyRandom.nextInt(2);
                    }

                    actors[i][j] = new Actor(ind++, 2, from, to, MyRandom.nextInt(10));
                    actors[i][j].methodName = "transfer";
                }
            }
        }

        return actors;
    }
}