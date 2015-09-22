package com.siso.app.chat.utils;

import com.siso.app.chat.domain.User;
import com.siso.app.ui.MyApplication;
import com.siso.app.ui.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;


public class UserUtils {
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static User getUserInfo(String username){
    	Log.i("tag", "username=="+username);
        User user = MyApplication.getInstance().getContactList().get(username);
        if(user == null){
            user = new User(username);
        }
            
        if(user != null){
            //demo没有这些数据，临时填充
            user.setNick(username);
//            user.setAvatar("http://downloads.easemob.com/downloads/57.png");
        }
        return user;
    }
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
        User user = getUserInfo(username);
        if(user != null){
            Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.ic_avatar).into(imageView);
        }else{
            Picasso.with(context).load(R.drawable.ic_avatar).into(imageView);
        }
    }
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView,String avatarUrl){
        User user = getUserInfo(username);
        if(user != null){
            Picasso.with(context).load(avatarUrl).placeholder(R.drawable.ic_avatar).into(imageView);
        }else{
            Picasso.with(context).load(R.drawable.ic_avatar).into(imageView);
        }
    }
    
}
