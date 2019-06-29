package com.zfy.component.basic.app.view;

/**
 * CreateAt : 2018/10/9
 * Describe :
 *
 * @author chendong
 */
public class ViewOpts {

    private int   layout;
    private int   vmId;
    private Class vmClazz;
    private Class pClazz;

    private ViewOpts() {
    }

    public static ViewOpts makeMvvm(int layout, int vmId, Class vmClazz) {
        ViewOpts viewConfig = new ViewOpts();
        viewConfig.layout = layout;
        viewConfig.vmId = vmId;
        viewConfig.vmClazz = vmClazz;
        return viewConfig;
    }

    public static ViewOpts makeMvvm(int layout, Class vmClazz) {
        ViewOpts viewConfig = new ViewOpts();
        viewConfig.layout = layout;
        viewConfig.vmClazz = vmClazz;
        return viewConfig;
    }

    public static ViewOpts makeMvp(int layout, Class pClazz) {
        ViewOpts viewConfig = new ViewOpts();
        viewConfig.layout = layout;
        viewConfig.pClazz = pClazz;
        return viewConfig;
    }

    public static ViewOpts makeEmpty() {
        return new ViewOpts();
    }

    public static ViewOpts withLayout(int layout) {
        ViewOpts viewOpts = new ViewOpts();
        viewOpts.layout = layout;
        return viewOpts;
    }


    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public int getVmId() {
        return vmId;
    }

    public void setVmId(int vmId) {
        this.vmId = vmId;
    }

    public Class getVmClazz() {
        return vmClazz;
    }

    public void setVmClazz(Class vmClazz) {
        this.vmClazz = vmClazz;
    }

    public Class getpClazz() {
        return pClazz;
    }

    public void setpClazz(Class pClazz) {
        this.pClazz = pClazz;
    }
}
