package org.example.controllers;

import org.example.entity.Group;

import java.util.List;

public class GroupController extends BaseController{

    public List<Group> getGroupsOfMember(int idMember) {
        return Group.getListGroups(idMember);
    }

    public void updateGroup(Group newGroup) {
        Group.updateGroup(newGroup.getId(), newGroup.getName(), newGroup.getDesc());
    }
}
