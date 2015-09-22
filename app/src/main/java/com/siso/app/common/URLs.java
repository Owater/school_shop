/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.common;

/**
 * description : API URL
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-15 下午1:27:39
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-15 下午1:27:39 
 *
 */
public class URLs {
	
//	public final static String HOST = "172.16.111.50:8080/school";
//	public final static String HTTP = "http://";
	
	public final static String HOST = "school-owater.rhcloud.com";
	public final static String HTTP = "https://";

	private final static String URL_API_HOST = HTTP + HOST;
	
	/**
	 * 发布产品
	 */
	public final static String MARKET_PUBLISH_URL = URL_API_HOST+"/goodController/publishGoods";
	/**
	 * 商品列表
	 */
	public final static String MARKET_GOODSLIST_URL = URL_API_HOST+"/goodController/getGoodList?sortCode=";
	/**
	 * 增加评论
	 */
	public final static String MARKET_ADDCOMMENT_URL = URL_API_HOST+"/goodController/addComment";
	public final static String MARKET_COMMENTLIST_URL = URL_API_HOST+"/goodController/getGoodCommentList?goodId=";
	/**
	 * 用户注册
	 * 提交方式：post
	 * 参数：userPhone 用户手机号码
	 *       password 用户(两次MD5加密的)注册密码
	 */
	public final static String REGIRSTER = URL_API_HOST +"/registerController/register";
	
	/**
	 * 检查用户是否已经注册过了
	 * 提交方式：post
	 * 参数：userPhone 用户填写的手机号码
	 */
	public final static String CHECK_PHONE = URL_API_HOST + "/registerController/findPhone";

	/**
	 * 用户登录
	 * 提交方式：post
	 * 参数：userPhone 用户登陆的手机号码
	 *       password 用户(两次MD5加密的)登录密码
	 */
	public final static String LOGIN = URL_API_HOST + "/loginController/logins";
	
	/**
	 * 选择学校
	 */
	public final static String CHOOSE_SCHOOL = URL_API_HOST + "/goodController/getSchool";
	/**
	 * 搜索学校
	 */
	public final static String SEARCH_CHOOSE_SCHOOL = URL_API_HOST + "/goodController/searchSchool?s=";
	/**
	 * 七牛token
	 */
	public final static String QINIU_TOKEN = URL_API_HOST + "/goodController/getQiniu";
	/**
	 * 用户信息
	 */
	public final static String USERINFO = URL_API_HOST + "/userController/getUserInfoByUserId";
	public final static String USERINFO_GENER = URL_API_HOST + "/userController/getGeneralUserInfoByUserId";
	public final static String USERRELY = URL_API_HOST + "/userController/getUserReply";
	public final static String USERJOINTHEME= URL_API_HOST + "/userController/getUserJoinTheme";
	public final static String USER_RESETPWD= URL_API_HOST + "/userController/resetPassword";
	public final static String USER_UPDATEMSG= URL_API_HOST + "/userController/updateUserMsg";
	public final static String USER_FEEDBACK= URL_API_HOST + "/userController/feedback";
	public final static String USER_AVATAR= URL_API_HOST + "/userController/changeUserAvatar";
	public final static String USER_PUB_GOODS= URL_API_HOST + "/userController/getUserGoodsList";
	
	/**
	 * 分享
	 */
	public final static String SHARE_GOODS= URL_API_HOST + "/shareController/getShareGoods";
	public final static String SHARE_ADD= URL_API_HOST + "/shareController/addShare";
	public final static String SHARE_LIST= URL_API_HOST + "/shareController/getShareList";
	/**
	 * 发现
	 */
	public final static String MARK_PUB= URL_API_HOST + "/findController/addFind";
	public final static String MARK_LIST= URL_API_HOST + "/findController/getFindList";
	
	/**
	 * 搜索
	 */
	public final static String SEARCH= URL_API_HOST + "/goodController/searchGoods";
	public final static String CHANGE_GOODS_STATUS = URL_API_HOST + "/goodController/changeGoodsStatus";
	
	/**
	 * 收藏
	 */
	public final static String GOODS_LIKE = URL_API_HOST + "/goodController/changeLikeGoods";
}

	