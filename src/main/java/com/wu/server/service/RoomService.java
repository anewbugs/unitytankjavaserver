package com.wu.server.service;

import com.wu.server.bean.Room;
import com.wu.server.proto.MsgGetRoomList;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.RoomInfo;

import java.util.HashMap;

public class RoomService {
    //最大id
    private static int maxId = 1;
    //房间列表
    public static HashMap<Integer, Room> rooms = new HashMap<>();

    //创建房间
    public static Room AddRoom()
    {
        maxId++;
        Room room = new Room();
        room.id = maxId;
        rooms.put(room.id, room);
        return room;
    }

    //删除房间
    public static boolean RemoveRoom(int id)
    {
        rooms.remove(id);
        return true;
    }

    //获取房间
    public static Room GetRoom(int id)
    {
        if (rooms.containsKey(id))
        {
            return rooms.get(id);
        }
        return null;
    }

    //生成MsgGetRoomList协议
    public static MsgBase ToMsg()
    {
        MsgGetRoomList msg = new MsgGetRoomList();
        int count = rooms.size();
        msg.rooms = new RoomInfo[count];
        //rooms
        int i = 0;
        for (Room room : rooms.values())
        {
            RoomInfo roomInfo = new RoomInfo();
            //赋值
            roomInfo.id = room.id;
            roomInfo.count = room.playerIds.size();
            roomInfo.status = room.status;

            msg.rooms[i] = roomInfo;
            i++;
        }
        return msg;
    }

    //Update
    public static void Update()
    {
        for (Room room : rooms.values())
        {
            room.Update();
        }
    }
}
