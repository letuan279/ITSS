package org.example.controllers;

import org.example.entity.Group;
import org.example.entity.User;

import java.util.Iterator;
import java.util.List;

public class GroupController extends BaseController{

    public List<Group> getGroupsOfMember(int idMember) {
        return Group.getListGroups(idMember);
    }

    public void updateGroup(Group newGroup) {
        Group.updateGroup(newGroup.getId(), newGroup.getName(), newGroup.getDesc());
    }

    public List<User> getMembers(int idCreateUser) {
        return Group.getMembers(idCreateUser);
    }

    public Group addGroup(String name, String desc, List<Integer> userIds, int idLeader){
        return Group.addGroup(name, desc, userIds, idLeader);
    }

    public List<User> getMemberInGroup(int idGroup) {
        return Group.getMemberInGroup(idGroup);
    }

    public boolean isLeader(int idUser, int idGroup) {
        return Group.isLeader(idUser, idGroup);
    }

    public void deleteMember(int idMember, int idGroup){
        Group.deleteMember(idMember, idGroup);
    }

    public List<User> getMemberAddToGroup(int idGroup) {
        return Group.getMemberAddToGroup(idGroup);
    }

    public void addMembers(int idGroup, List<Integer> idMembers) {
        Group.addMembers(idGroup, idMembers);
    }
}
