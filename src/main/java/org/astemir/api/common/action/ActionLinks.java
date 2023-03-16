package org.astemir.api.common.action;

public class ActionLinks {

    public ActionLinks.Start onStart;
    public ActionLinks.Tick onTick;
    public ActionLinks.End onEnd;

    public void start(ActionController controller){
        if (onStart != null){
            onStart.run(controller);
        }
    }

    public void tick(ActionController controller,int tick){
        if (onTick != null){
            onTick.run(controller,tick);
        }
    }

    public void end(ActionController controller){
        if (onEnd != null){
            onEnd.run(controller);
        }
    }

    public interface Start{
        void run(ActionController controller);
    }

    public interface Tick{
        void run(ActionController controller,int tick);
    }

    public interface End{
        void run(ActionController controller);
    }
}
