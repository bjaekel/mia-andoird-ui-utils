package de.sevenfactory.mia.modules.irregulargrid;

/**
 * Created by jke0001b on 18.02.2016.
 */
public interface IrregularGridModel {

    String getId();

    String getTitle();

    String getSubTitle();

    ImageListModel getImages();

    long getDuration();

    long getAirDate();

    String getBrand();

    boolean usesColoredBrandIcon();
}
