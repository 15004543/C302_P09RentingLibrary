package sg.edu.rp.c347.c302_p09rentinglibrary;

/**
 * Created by 15004543 on 18/7/2017.
 */

public class BookCategory {
    private int cat_id;
    private String name;

    public BookCategory(){
        super();
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
