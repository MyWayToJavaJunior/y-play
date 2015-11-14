package yplay.gui;

import com.sun.javafx.scene.control.skin.ListViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.scene.control.ListView;

public class GuiUtils {
    public static int getFirstVisibleItem(ListView<?> t) {
        try {
            ListViewSkin<?> ts = (ListViewSkin<?>) t.getSkin();
            VirtualFlow<?> vf = (VirtualFlow<?>) ts.getChildren().get(0);
            return vf.getFirstVisibleCell().getIndex();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static int getLastVisibleItem(ListView<?> t) {
        try {
            ListViewSkin<?> ts = (ListViewSkin<?>) t.getSkin();
            VirtualFlow<?> vf = (VirtualFlow<?>) ts.getChildren().get(0);
            return vf.getLastVisibleCell().getIndex();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static boolean isLastElementVisible(ListView<?> t) {
        int lastVisible = getLastVisibleItem(t);
        int size = t.getItems().size() - 1;
        return (lastVisible != -1 &&  size > 0 && size <= lastVisible);
    }

    public static boolean isLastElementSelected(ListView<?> t) {
        return (t.getSelectionModel().getSelectedIndex() == t.getItems().size() - 1);
    }
}
