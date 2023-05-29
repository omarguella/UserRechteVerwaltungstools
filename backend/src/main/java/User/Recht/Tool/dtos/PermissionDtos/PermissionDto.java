package User.Recht.Tool.dtos.PermissionDtos;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PermissionDto {

    @NotNull
    private String name;

    private List<String> listOfAction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getListOfAction() {
        return listOfAction;
    }

    public void setListOfAction(List<String> listOfAction) {
        this.listOfAction = listOfAction;
    }

    @Override
    public String toString() {
        return "PermissionDto{" +
                "name='" + name + '\'' +
                ", listOfAction=" + listOfAction +
                '}';
    }
}

