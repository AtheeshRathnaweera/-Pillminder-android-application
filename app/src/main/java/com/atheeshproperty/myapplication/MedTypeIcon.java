package com.atheeshproperty.myapplication;

public class MedTypeIcon {

    private int tablet;
    private int capsule;
    private int liquid;
    private int gel;
    private int injection;
    private int powder;

    public MedTypeIcon(){

    }

    public MedTypeIcon(Integer tableti,Integer capsulei, Integer liquidi, Integer geli,
                       Integer injectioni, Integer powderi){

        tablet = tableti;
        capsule = capsulei;
        liquid = liquidi;
        gel = geli;
        injection = injectioni;
        powder = powderi;

    }

    public int getTablet() {
        return tablet;
    }

    public void setTablet(int tablet) {
        this.tablet = tablet;
    }

    public int getCapsule() {
        return capsule;
    }

    public void setCapsule(int capsule) {
        this.capsule = capsule;
    }

    public int getLiquid() {
        return liquid;
    }

    public void setLiquid(int liquid) {
        this.liquid = liquid;
    }

    public int getGel() {
        return gel;
    }

    public void setGel(int gel) {
        this.gel = gel;
    }

    public int getInjection() {
        return injection;
    }

    public void setInjection(int injection) {
        this.injection = injection;
    }

    public int getPowder() {
        return powder;
    }

    public void setPowder(int powder) {
        this.powder = powder;
    }
}
