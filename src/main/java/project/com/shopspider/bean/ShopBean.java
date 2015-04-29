package com.shopspider.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shops")
public class ShopBean implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3095176079237586508L;

	/**
	 * 数据库shop id
	 */
	private int shopId = -1;

	private String shopName = null;

	private String sellerName = null;

	/**
	 * 淘宝的shopId
	 */
	private String srcShopId = null;

	private String location = null;

	/**
	 * 业务
	 */
	private String business = null;

	/**
	 * 主营
	 */
	private String mainBusiness = null;

	/**
	 * 主营
	 */
	private int mainBusinessCreditValue = -1;

	/**
	 * 主营占比,%
	 */
	private double mainBusinessRate = -1;

	private long saleCount = -1;

	private int productCount = -1;

	/**
	 * 优惠数量 如果为21，则说明20+
	 */
	private int newProductCount = -1;

	/**
	 * 优惠数量 如果为21，则说明优惠20+
	 */
	private int promotionCount = -1;

	/**
	 * Taobao(0), Tmall(1), Unknow(2);
	 */
	private ShopType shopType = ShopType.Unknow;

	/**
	 * shop建立时间
	 */
	private Date establishAt = null;

	/**
	 * 第一次抓取时间
	 */
	private Date createdAt = null;

	/**
	 * 最后更新时间
	 */
	private Date updatedAt = null;

	/**
	 * 商家特殊服务 如：承诺7日无理由退换货
	 */
	private String services = null;

	/**
	 * 好评率，%
	 */
	private double praiseRate = -1;

	/**
	 * 评分，满分5
	 */
	private double praiseScore = -1;

	/**
	 * 描述相符，满分5
	 */
	private double descriptionRight = -1;

	/**
	 * 与同行于平均水平相比，正数高于，负数低于
	 */
	private double descriptionRightCompared = -101;

	/**
	 * 描述相符评论人数
	 */
	private double descriptionRightScoreCount = -1;

	/**
	 * 打1分人占比
	 */
	private double descriptionRight1Rate = -1;
	private double descriptionRight2Rate = -1;
	private double descriptionRight3Rate = -1;
	private double descriptionRight4Rate = -1;
	private double descriptionRight5Rate = -1;

	/**
	 * 服务态度，满分5
	 */
	private double serviceAttitude = -1;
	/**
	 * 与同行于平均水平相比，正数高于，负数低于
	 */
	private double serviceAttitudeCompared = -101;

	/**
	 * 服务态度评论人数
	 */
	private double serviceAttitudeScoreCount = -1;

	/**
	 * 打1分人占比
	 */
	private double serviceAttitude1Rate = -1;
	private double serviceAttitude2Rate = -1;
	private double serviceAttitude3Rate = -1;
	private double serviceAttitude4Rate = -1;
	private double serviceAttitude5Rate = -1;

	/**
	 * 发货速度，满分5
	 */
	private double deliverySpeed = -1;
	/**
	 * 与同行于平均水平相比，正数高于，负数低于
	 */
	private double deliverySpeedCompared = -101;

	/**
	 * 发货速度评论人数
	 */
	private double deliverySpeedScoreCount = -1;

	/**
	 * 打1分人占比
	 */
	private double deliverySpeed1Rate = -1;
	private double deliverySpeed2Rate = -1;
	private double deliverySpeed3Rate = -1;
	private double deliverySpeed4Rate = -1;
	private double deliverySpeed5Rate = -1;

	private String shopUrl = null;

	private String shopRateUrl = null;

	/**
	 * 卖家信用
	 */
	private String shopSellerCreditPic = null;

	private int shopSellerCreditValue = -1;
	private int shopSellerCreditLevel = -1;

	/**
	 * 买家信用
	 */
	private String shopBuyerCreditPic = null;

	private int shopBuyerCreditValue = -1;
	private int shopBuyerCreditLevel = -1;

	/**
	 * 保证金余额
	 */
	private double chrgeBalance = -1;

	/**
	 * 30天内平均退款速度,天
	 */
	private double averageRefundSpeed = -1;
	/**
	 * 同行业30天内平均退款速度,天
	 */
	private double averageRefundSpeedOfBussiness = -1;
	/**
	 * 30天内退款率,%
	 */
	private double refundRate = -1;
	/**
	 * 同行业30天内退款率,%
	 */
	private double refundRateOfBussiness = -1;

	/**
	 * 30天内退款数量
	 */
	private int refundCount = -1;
	/**
	 * 30天内因质量问题退款数量
	 */
	private int qualityRefundCount = -1;

	/**
	 * 30天内没收到货退款数量
	 */
	private int noReceiveRefundCount = -1;

	/**
	 * 30天内无理由退款数量
	 */
	private int noReasonRefundCount = -1;

	/**
	 * 30天内投诉率,%
	 */
	private double complaintRate = -1;
	/**
	 * 同行业30天内投诉率,%
	 */
	private double complaintRateOfBussiness = -1;
	/**
	 * 30天内投诉次数
	 */
	private int complaintCount = -1;
	/**
	 * 30天内因售后问题被投诉次数
	 */
	private int afterSaleComplaintCount = -1;
	/**
	 * 30天内因行为违规被投诉次数
	 */
	private int violationComplaintCount = -1;

	/**
	 * 30天内处罚数
	 */
	private double punishCount = -1;
	/**
	 * 同行业30天内处罚率,%
	 */
	private double punishCountOfBussiness = -1;

	/**
	 * 30天内因虚假交易被处罚次数
	 */
	private int falseMerchPunishCount = -1;

	/**
	 * 30天内因侵犯知识产权被处罚次数
	 */
	private int infringementPunishCount = -1;

	/**
	 * 30天内因发布违禁信息被处罚次数
	 */
	private int prohibitedInfoPunishCount = -1;

	/**
	 * 公司名称，天猫才有
	 */
	private String companyName;

	/**
	 * 最近一个月总数的好评数
	 */
	private int totalOkCountOneMonth = -1;

	/**
	 * 最近一个月总数的中评数
	 */
	private int totalNormalCountOneMonth = -1;

	/**
	 * 最近一个月总数的差评数
	 */
	private int totalBadCountOneMonth = -1;

	/**
	 * 最近一个月主营的好评数
	 */
	private int mainBusOkCountOneMonth = -1;

	/**
	 * 最近一个月主营的中评数
	 */
	private int mainBusNormalCountOneMonth = -1;

	/**
	 * 最近一个月主营的差评数
	 */
	private int mainBusBadCountOneMonth = -1;

	/**
	 * 最近一个月非主营的好评数
	 */
	private int notMainBusOkCountOneMonth = -1;

	/**
	 * 最近一个月非主营的中评数
	 */
	private int notMainBusNormalCountOneMonth = -1;

	/**
	 * 最近一个月非主营的差评数
	 */
	private int notMainBusBadCountOneMonth = -1;

	/**
	 * 最近半年总数的好评数
	 */
	private int totalOkCountInHalfYear = -1;

	/**
	 * 最近半年总数的中评数
	 */
	private int totalNormalCountInHalfYear = -1;

	/**
	 * 最近半年总数的差评数
	 */
	private int totalBadCountInHalfYear = -1;

	/**
	 * 最近半年主营的好评数
	 */
	private int mainBusOkCountInHalfYear = -1;

	/**
	 * 最近半年主营的中评数
	 */
	private int mainBusNormalCountInHalfYear = -1;

	/**
	 * 最近半年主营的差评数
	 */
	private int mainBusBadCountInHalfYear = -1;

	/**
	 * 最近半年非主营的好评数
	 */
	private int notMainBusOkCountInHalfYear = -1;

	/**
	 * 最近半年非主营的中评数
	 */
	private int notMainBusNormalCountInHalfYear = -1;

	/**
	 * 最近半年非主营的差评数
	 */
	private int notMainBusBadCountInHalfYear = -1;

	/**
	 * 半年以前总数的好评数
	 */
	private int totalOkCountBeforeHalfYear = -1;

	/**
	 * 半年以前总数的中评数
	 */
	private int totalNormalCountBeforeHalfYear = -1;

	/**
	 * 半年以前总数的差评数
	 */
	private int totalBadCountBeforeHalfYear = -1;

	/**
	 * 半年以前主营的好评数
	 */
	private int mainBusOkCountBeforeHalfYear = -1;

	/**
	 * 半年以前主营的中评数
	 */
	private int mainBusNormalCountBeforeHalfYear = -1;

	/**
	 * 半年以前主营的差评数
	 */
	private int mainBusBadCountBeforeHalfYear = -1;

	/**
	 * 半年以前非主营的好评数
	 */
	private int notMainBusOkCountBeforeHalfYear = -1;

	/**
	 * 半年以前非主营的中评数
	 */
	private int notMainBusNormalCountBeforeHalfYear = -1;

	/**
	 * 半年以前非主营的差评数
	 */
	private int notMainBusBadCountBeforeHalfYear = -1;

	@Column(name = "shop_name")
	public String getShopName()
	{
		return shopName;
	}

	public void setShopName(String shopName)
	{
		this.shopName = shopName;
	}

	@Column(name = "seller_name")
	public String getSellerName()
	{
		return sellerName;
	}

	public void setSellerName(String sellerName)
	{
		this.sellerName = sellerName;
	}

	@Column(name = "location")
	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	@Column(name = "business")
	public String getBusiness()
	{
		return business;
	}

	public void setBusiness(String business)
	{
		this.business = business;
	}

	@Column(name = "main_business")
	public String getMainBusiness()
	{
		return mainBusiness;
	}

	public void setMainBusiness(String mainBusiness)
	{
		this.mainBusiness = mainBusiness;
	}

	@Column(name = "main_business_rate")
	public double getMainBusinessRate()
	{
		return mainBusinessRate;
	}

	public void setMainBusinessRate(double mainBusinessRate)
	{
		this.mainBusinessRate = mainBusinessRate;
	}

	@Column(name = "sale_count")
	public long getSaleCount()
	{
		return saleCount;
	}

	public void setSaleCount(long saleCount)
	{
		this.saleCount = saleCount;
	}

	@Column(name = "product_count")
	public int getProductCount()
	{
		return productCount;
	}

	public void setProductCount(int productCount)
	{
		this.productCount = productCount;
	}

	@Column(name = "new_product_count")
	public int getNewProductCount()
	{
		return newProductCount;
	}

	public void setNewProductCount(int newProductCount)
	{
		this.newProductCount = newProductCount;
	}

	@Column(name = "promotion_count")
	public int getPromotionCount()
	{
		return promotionCount;
	}

	public void setPromotionCount(int promotionCount)
	{
		this.promotionCount = promotionCount;
	}

	@Column(name = "shop_type")
	@Enumerated(EnumType.ORDINAL)
	public ShopType getShopType()
	{
		return shopType;
	}

	// public ShopType getShopType()
	// {
	// return shopType;
	// }

	public void setShopType(ShopType shopType)
	{
		this.shopType = shopType;
	}

	@Column(name = "establish_at")
	public Date getEstablishAt()
	{
		return establishAt;
	}

	public void setEstablishAt(Date establishAt)
	{
		this.establishAt = establishAt;
	}

	@Column(name = "created_at")
	public Date getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(Date createdAt)
	{
		this.createdAt = createdAt;
	}

	@Column(name = "updated_at")
	public Date getUpdatedAt()
	{
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt)
	{
		this.updatedAt = updatedAt;
	}

	@Column(name = "services")
	public String getServices()
	{
		return services;
	}

	public void setServices(String services)
	{
		this.services = services;
	}

	@Column(name = "praise_rate")
	public double getPraiseRate()
	{
		return praiseRate;
	}

	public void setPraiseRate(double praiseRate)
	{
		this.praiseRate = praiseRate;
	}

	@Column(name = "praise_score")
	public double getPraiseScore()
	{
		return praiseScore;
	}

	public void setPraiseScore(double praiseScore)
	{
		this.praiseScore = praiseScore;
	}

	@Column(name = "description_right")
	public double getDescriptionRight()
	{
		return descriptionRight;
	}

	public void setDescriptionRight(double descriptionRight)
	{
		this.descriptionRight = descriptionRight;
	}

	@Column(name = "description_right_compared")
	public double getDescriptionRightCompared()
	{
		return descriptionRightCompared;
	}

	public void setDescriptionRightCompared(double descriptionRightCompared)
	{
		this.descriptionRightCompared = descriptionRightCompared;
	}

	@Column(name = "service_attitude")
	public double getServiceAttitude()
	{
		return serviceAttitude;
	}

	public void setServiceAttitude(double serviceAttitude)
	{
		this.serviceAttitude = serviceAttitude;
	}

	@Column(name = "service_attitude_compared")
	public double getServiceAttitudeCompared()
	{
		return serviceAttitudeCompared;
	}

	public void setServiceAttitudeCompared(double serviceAttitudeCompared)
	{
		this.serviceAttitudeCompared = serviceAttitudeCompared;
	}

	@Column(name = "delivery_speed")
	public double getDeliverySpeed()
	{
		return deliverySpeed;
	}

	public void setDeliverySpeed(double deliverySpeed)
	{
		this.deliverySpeed = deliverySpeed;
	}

	@Column(name = "delivery_speed_compared")
	public double getDeliverySpeedCompared()
	{
		return deliverySpeedCompared;
	}

	public void setDeliverySpeedCompared(double deliverySpeedCompared)
	{
		this.deliverySpeedCompared = deliverySpeedCompared;
	}

	@Column(name = "shop_url")
	public String getShopUrl()
	{
		return shopUrl;
	}

	public void setShopUrl(String shopUrl)
	{
		this.shopUrl = shopUrl;
	}

	@Column(name = "shop_rate_url")
	public String getShopRateUrl()
	{
		return shopRateUrl;
	}

	public void setShopRateUrl(String shopRateUrl)
	{
		this.shopRateUrl = shopRateUrl;
	}

	@Column(name = "shop_seller_credit_pic")
	public String getShopSellerCreditPic()
	{
		return shopSellerCreditPic;
	}

	public void setShopSellerCreditPic(String shopSellerCreditPic)
	{
		this.shopSellerCreditPic = shopSellerCreditPic;
	}

	@Column(name = "shop_seller_credit_value")
	public int getShopSellerCreditValue()
	{
		return shopSellerCreditValue;
	}

	public void setShopSellerCreditValue(int shopSellerCreditValue)
	{
		this.shopSellerCreditValue = shopSellerCreditValue;
	}

	@Column(name = "shop_seller_credit_level")
	public int getShopSellerCreditLevel()
	{
		return shopSellerCreditLevel;
	}

	public void setShopSellerCreditLevel(int shopSellerCreditLevel)
	{
		this.shopSellerCreditLevel = shopSellerCreditLevel;
	}

	@Column(name = "shop_buyer_credit_pic")
	public String getShopBuyerCreditPic()
	{
		return shopBuyerCreditPic;
	}

	public void setShopBuyerCreditPic(String shopBuyerCreditPic)
	{
		this.shopBuyerCreditPic = shopBuyerCreditPic;
	}

	@Column(name = "shop_buyer_credit_value")
	public int getShopBuyerCreditValue()
	{
		return shopBuyerCreditValue;
	}

	public void setShopBuyerCreditValue(int shopBuyerCreditValue)
	{
		this.shopBuyerCreditValue = shopBuyerCreditValue;
	}

	@Column(name = "shop_buyer_credit_level")
	public int getShopBuyerCreditLevel()
	{
		return shopBuyerCreditLevel;
	}

	public void setShopBuyerCreditLevel(int shopBuyerCreditLevel)
	{
		this.shopBuyerCreditLevel = shopBuyerCreditLevel;
	}

	@Column(name = "chrge_balance")
	public double getChrgeBalance()
	{
		return chrgeBalance;
	}

	public void setChrgeBalance(double chrgeBalance)
	{
		this.chrgeBalance = chrgeBalance;
	}

	@Column(name = "average_refund_speed")
	public double getAverageRefundSpeed()
	{
		return averageRefundSpeed;
	}

	public void setAverageRefundSpeed(double averageRefundSpeed)
	{
		this.averageRefundSpeed = averageRefundSpeed;
	}

	@Column(name = "average_refund_speed_of_bussiness")
	public double getAverageRefundSpeedOfBussiness()
	{
		return averageRefundSpeedOfBussiness;
	}

	public void setAverageRefundSpeedOfBussiness(
	        double averageRefundSpeedOfBussiness)
	{
		this.averageRefundSpeedOfBussiness = averageRefundSpeedOfBussiness;
	}

	@Column(name = "refund_rate")
	public double getRefundRate()
	{
		return refundRate;
	}

	public void setRefundRate(double refundRate)
	{
		this.refundRate = refundRate;
	}

	@Column(name = "refund_rate_of_bussiness")
	public double getRefundRateOfBussiness()
	{
		return refundRateOfBussiness;
	}

	public void setRefundRateOfBussiness(double refundRateOfBussiness)
	{
		this.refundRateOfBussiness = refundRateOfBussiness;
	}

	@Column(name = "complaint_rate")
	public double getComplaintRate()
	{
		return complaintRate;
	}

	public void setComplaintRate(double complaintRate)
	{
		this.complaintRate = complaintRate;
	}

	@Column(name = "complaint_rate_of_bussiness")
	public double getComplaintRateOfBussiness()
	{
		return complaintRateOfBussiness;
	}

	public void setComplaintRateOfBussiness(double complaintRateOfBussiness)
	{
		this.complaintRateOfBussiness = complaintRateOfBussiness;
	}

	@Column(name = "punish_count")
	public double getPunishCount()
	{
		return punishCount;
	}

	public void setPunishCount(double punishCount)
	{
		this.punishCount = punishCount;
	}

	@Column(name = "punish_count_of_bussiness")
	public double getPunishCountOfBussiness()
	{
		return punishCountOfBussiness;
	}

	public void setPunishCountOfBussiness(double punishCountOfBussiness)
	{
		this.punishCountOfBussiness = punishCountOfBussiness;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getShopId()
	{
		return shopId;
	}

	public void setShopId(int shopId)
	{
		this.shopId = shopId;
	}

	@Column(name = "src_shop_id")
	public String getSrcShopId()
	{
		return srcShopId;
	}

	public void setSrcShopId(String srcShopId)
	{
		this.srcShopId = srcShopId;
	}

	@Column(name = "company_name")
	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	@Column(name = "description_right_score_count")
	public double getDescriptionRightScoreCount()
	{
		return descriptionRightScoreCount;
	}

	public void setDescriptionRightScoreCount(double descriptionRightScoreCount)
	{
		this.descriptionRightScoreCount = descriptionRightScoreCount;
	}

	@Column(name = "description_right_1_rate")
	public double getDescriptionRight1Rate()
	{
		return descriptionRight1Rate;
	}

	public void setDescriptionRight1Rate(double descriptionRight1Rate)
	{
		this.descriptionRight1Rate = descriptionRight1Rate;
	}

	@Column(name = "description_right_2_rate")
	public double getDescriptionRight2Rate()
	{
		return descriptionRight2Rate;
	}

	public void setDescriptionRight2Rate(double descriptionRight2Rate)
	{
		this.descriptionRight2Rate = descriptionRight2Rate;
	}

	@Column(name = "description_right_3_rate")
	public double getDescriptionRight3Rate()
	{
		return descriptionRight3Rate;
	}

	public void setDescriptionRight3Rate(double descriptionRight3Rate)
	{
		this.descriptionRight3Rate = descriptionRight3Rate;
	}

	@Column(name = "description_right_4_rate")
	public double getDescriptionRight4Rate()
	{
		return descriptionRight4Rate;
	}

	public void setDescriptionRight4Rate(double descriptionRight4Rate)
	{
		this.descriptionRight4Rate = descriptionRight4Rate;
	}

	@Column(name = "description_right_5_rate")
	public double getDescriptionRight5Rate()
	{
		return descriptionRight5Rate;
	}

	public void setDescriptionRight5Rate(double descriptionRight5Rate)
	{
		this.descriptionRight5Rate = descriptionRight5Rate;
	}

	@Column(name = "service_attitude_score_count")
	public double getServiceAttitudeScoreCount()
	{
		return serviceAttitudeScoreCount;
	}

	public void setServiceAttitudeScoreCount(double serviceAttitudeScoreCount)
	{
		this.serviceAttitudeScoreCount = serviceAttitudeScoreCount;
	}

	@Column(name = "service_attitude_1_rate")
	public double getServiceAttitude1Rate()
	{
		return serviceAttitude1Rate;
	}

	public void setServiceAttitude1Rate(double serviceAttitude1Rate)
	{
		this.serviceAttitude1Rate = serviceAttitude1Rate;
	}

	@Column(name = "service_attitude_2_rate")
	public double getServiceAttitude2Rate()
	{
		return serviceAttitude2Rate;
	}

	public void setServiceAttitude2Rate(double serviceAttitude2Rate)
	{
		this.serviceAttitude2Rate = serviceAttitude2Rate;
	}

	@Column(name = "service_attitude_3_rate")
	public double getServiceAttitude3Rate()
	{
		return serviceAttitude3Rate;
	}

	public void setServiceAttitude3Rate(double serviceAttitude3Rate)
	{
		this.serviceAttitude3Rate = serviceAttitude3Rate;
	}

	@Column(name = "service_attitude_4_rate")
	public double getServiceAttitude4Rate()
	{
		return serviceAttitude4Rate;
	}

	public void setServiceAttitude4Rate(double serviceAttitude4Rate)
	{
		this.serviceAttitude4Rate = serviceAttitude4Rate;
	}

	@Column(name = "service_attitude_5_rate")
	public double getServiceAttitude5Rate()
	{
		return serviceAttitude5Rate;
	}

	public void setServiceAttitude5Rate(double serviceAttitude5Rate)
	{
		this.serviceAttitude5Rate = serviceAttitude5Rate;
	}

	@Column(name = "delivery_speed_score_count")
	public double getDeliverySpeedScoreCount()
	{
		return deliverySpeedScoreCount;
	}

	public void setDeliverySpeedScoreCount(double deliverySpeedScoreCount)
	{
		this.deliverySpeedScoreCount = deliverySpeedScoreCount;
	}

	@Column(name = "delivery_speed_1_rate")
	public double getDeliverySpeed1Rate()
	{
		return deliverySpeed1Rate;
	}

	public void setDeliverySpeed1Rate(double deliverySpeed1Rate)
	{
		this.deliverySpeed1Rate = deliverySpeed1Rate;
	}

	@Column(name = "delivery_speed_2_rate")
	public double getDeliverySpeed2Rate()
	{
		return deliverySpeed2Rate;
	}

	public void setDeliverySpeed2Rate(double deliverySpeed2Rate)
	{
		this.deliverySpeed2Rate = deliverySpeed2Rate;
	}

	@Column(name = "delivery_speed_3_rate")
	public double getDeliverySpeed3Rate()
	{
		return deliverySpeed3Rate;
	}

	public void setDeliverySpeed3Rate(double deliverySpeed3Rate)
	{
		this.deliverySpeed3Rate = deliverySpeed3Rate;
	}

	@Column(name = "delivery_speed_4_rate")
	public double getDeliverySpeed4Rate()
	{
		return deliverySpeed4Rate;
	}

	public void setDeliverySpeed4Rate(double deliverySpeed4Rate)
	{
		this.deliverySpeed4Rate = deliverySpeed4Rate;
	}

	@Column(name = "delivery_speed_5_rate")
	public double getDeliverySpeed5Rate()
	{
		return deliverySpeed5Rate;
	}

	public void setDeliverySpeed5Rate(double deliverySpeed5Rate)
	{
		this.deliverySpeed5Rate = deliverySpeed5Rate;
	}

	@Column(name = "main_business_credit_value")
	public int getMainBusinessCreditValue()
	{
		return mainBusinessCreditValue;
	}

	public void setMainBusinessCreditValue(int mainBusinessCreditValue)
	{
		this.mainBusinessCreditValue = mainBusinessCreditValue;
	}

	@Column(name = "total_ok_count_one_month")
	public int getTotalOkCountOneMonth()
	{
		return totalOkCountOneMonth;
	}

	public void setTotalOkCountOneMonth(int totalOkCountOneMonth)
	{
		this.totalOkCountOneMonth = totalOkCountOneMonth;
	}

	@Column(name = "total_normal_count_one_month")
	public int getTotalNormalCountOneMonth()
	{
		return totalNormalCountOneMonth;
	}

	public void setTotalNormalCountOneMonth(int totalNormalCountOneMonth)
	{
		this.totalNormalCountOneMonth = totalNormalCountOneMonth;
	}

	@Column(name = "total_bad_count_one_month")
	public int getTotalBadCountOneMonth()
	{
		return totalBadCountOneMonth;
	}

	public void setTotalBadCountOneMonth(int totalBadCountOneMonth)
	{
		this.totalBadCountOneMonth = totalBadCountOneMonth;
	}

	@Column(name = "main_bus_ok_count_one_month")
	public int getMainBusOkCountOneMonth()
	{
		return mainBusOkCountOneMonth;
	}

	public void setMainBusOkCountOneMonth(int mainBusOkCountOneMonth)
	{
		this.mainBusOkCountOneMonth = mainBusOkCountOneMonth;
	}

	@Column(name = "main_bus_normal_count_one_month")
	public int getMainBusNormalCountOneMonth()
	{
		return mainBusNormalCountOneMonth;
	}

	public void setMainBusNormalCountOneMonth(int mainBusNormalCountOneMonth)
	{
		this.mainBusNormalCountOneMonth = mainBusNormalCountOneMonth;
	}

	@Column(name = "main_bus_bad_count_one_month")
	public int getMainBusBadCountOneMonth()
	{
		return mainBusBadCountOneMonth;
	}

	public void setMainBusBadCountOneMonth(int mainBusBadCountOneMonth)
	{
		this.mainBusBadCountOneMonth = mainBusBadCountOneMonth;
	}

	@Column(name = "not_main_bus_ok_count_one_month")
	public int getNotMainBusOkCountOneMonth()
	{
		return notMainBusOkCountOneMonth;
	}

	public void setNotMainBusOkCountOneMonth(int notMainBusOkCountOneMonth)
	{
		this.notMainBusOkCountOneMonth = notMainBusOkCountOneMonth;
	}

	@Column(name = "not_main_bus_normal_count_one_month")
	public int getNotMainBusNormalCountOneMonth()
	{
		return notMainBusNormalCountOneMonth;
	}

	public void setNotMainBusNormalCountOneMonth(
	        int notMainBusNormalCountOneMonth)
	{
		this.notMainBusNormalCountOneMonth = notMainBusNormalCountOneMonth;
	}

	@Column(name = "not_main_bus_bad_count_one_month")
	public int getNotMainBusBadCountOneMonth()
	{
		return notMainBusBadCountOneMonth;
	}

	public void setNotMainBusBadCountOneMonth(int notMainBusBadCountOneMonth)
	{
		this.notMainBusBadCountOneMonth = notMainBusBadCountOneMonth;
	}

	@Column(name = "total_ok_count_in_half_year")
	public int getTotalOkCountInHalfYear()
	{
		return totalOkCountInHalfYear;
	}

	public void setTotalOkCountInHalfYear(int totalOkCountInHalfYear)
	{
		this.totalOkCountInHalfYear = totalOkCountInHalfYear;
	}

	@Column(name = "total_normal_count_in_half_year")
	public int getTotalNormalCountInHalfYear()
	{
		return totalNormalCountInHalfYear;
	}

	public void setTotalNormalCountInHalfYear(int totalNormalCountInHalfYear)
	{
		this.totalNormalCountInHalfYear = totalNormalCountInHalfYear;
	}

	@Column(name = "total_bad_count_in_half_year")
	public int getTotalBadCountInHalfYear()
	{
		return totalBadCountInHalfYear;
	}

	public void setTotalBadCountInHalfYear(int totalBadCountInHalfYear)
	{
		this.totalBadCountInHalfYear = totalBadCountInHalfYear;
	}

	@Column(name = "main_bus_ok_count_in_half_year")
	public int getMainBusOkCountInHalfYear()
	{
		return mainBusOkCountInHalfYear;
	}

	public void setMainBusOkCountInHalfYear(int mainBusOkCountInHalfYear)
	{
		this.mainBusOkCountInHalfYear = mainBusOkCountInHalfYear;
	}

	@Column(name = "main_bus_normal_count_in_half_year")
	public int getMainBusNormalCountInHalfYear()
	{
		return mainBusNormalCountInHalfYear;
	}

	public void setMainBusNormalCountInHalfYear(int mainBusNormalCountInHalfYear)
	{
		this.mainBusNormalCountInHalfYear = mainBusNormalCountInHalfYear;
	}

	@Column(name = "main_bus_bad_count_in_half_year")
	public int getMainBusBadCountInHalfYear()
	{
		return mainBusBadCountInHalfYear;
	}

	public void setMainBusBadCountInHalfYear(int mainBusBadCountInHalfYear)
	{
		this.mainBusBadCountInHalfYear = mainBusBadCountInHalfYear;
	}

	@Column(name = "not_main_bus_ok_count_in_half_year")
	public int getNotMainBusOkCountInHalfYear()
	{
		return notMainBusOkCountInHalfYear;
	}

	public void setNotMainBusOkCountInHalfYear(int notMainBusOkCountInHalfYear)
	{
		this.notMainBusOkCountInHalfYear = notMainBusOkCountInHalfYear;
	}

	@Column(name = "not_main_bus_normal_count_in_half_year")
	public int getNotMainBusNormalCountInHalfYear()
	{
		return notMainBusNormalCountInHalfYear;
	}

	public void setNotMainBusNormalCountInHalfYear(
	        int notMainBusNormalCountInHalfYear)
	{
		this.notMainBusNormalCountInHalfYear = notMainBusNormalCountInHalfYear;
	}

	@Column(name = "not_main_bus_bad_count_in_half_year")
	public int getNotMainBusBadCountInHalfYear()
	{
		return notMainBusBadCountInHalfYear;
	}

	public void setNotMainBusBadCountInHalfYear(int notMainBusBadCountInHalfYear)
	{
		this.notMainBusBadCountInHalfYear = notMainBusBadCountInHalfYear;
	}

	@Column(name = "total_ok_count_before_half_year")
	public int getTotalOkCountBeforeHalfYear()
	{
		return totalOkCountBeforeHalfYear;
	}

	public void setTotalOkCountBeforeHalfYear(int totalOkCountBeforeHalfYear)
	{
		this.totalOkCountBeforeHalfYear = totalOkCountBeforeHalfYear;
	}

	@Column(name = "total_normal_count_before_half_year")
	public int getTotalNormalCountBeforeHalfYear()
	{
		return totalNormalCountBeforeHalfYear;
	}

	public void setTotalNormalCountBeforeHalfYear(
	        int totalNormalCountBeforeHalfYear)
	{
		this.totalNormalCountBeforeHalfYear = totalNormalCountBeforeHalfYear;
	}

	@Column(name = "total_bad_count_before_half_year")
	public int getTotalBadCountBeforeHalfYear()
	{
		return totalBadCountBeforeHalfYear;
	}

	public void setTotalBadCountBeforeHalfYear(int totalBadCountBeforeHalfYear)
	{
		this.totalBadCountBeforeHalfYear = totalBadCountBeforeHalfYear;
	}

	@Column(name = "main_bus_ok_count_before_half_year")
	public int getMainBusOkCountBeforeHalfYear()
	{
		return mainBusOkCountBeforeHalfYear;
	}

	public void setMainBusOkCountBeforeHalfYear(int mainBusOkCountBeforeHalfYear)
	{
		this.mainBusOkCountBeforeHalfYear = mainBusOkCountBeforeHalfYear;
	}

	@Column(name = "main_bus_normal_count_before_half_year")
	public int getMainBusNormalCountBeforeHalfYear()
	{
		return mainBusNormalCountBeforeHalfYear;
	}

	public void setMainBusNormalCountBeforeHalfYear(
	        int mainBusNormalCountBeforeHalfYear)
	{
		this.mainBusNormalCountBeforeHalfYear = mainBusNormalCountBeforeHalfYear;
	}

	@Column(name = "main_bus_bad_count_before_half_year")
	public int getMainBusBadCountBeforeHalfYear()
	{
		return mainBusBadCountBeforeHalfYear;
	}

	public void setMainBusBadCountBeforeHalfYear(
	        int mainBusBadCountBeforeHalfYear)
	{
		this.mainBusBadCountBeforeHalfYear = mainBusBadCountBeforeHalfYear;
	}

	@Column(name = "not_main_bus_ok_count_before_half_year")
	public int getNotMainBusOkCountBeforeHalfYear()
	{
		return notMainBusOkCountBeforeHalfYear;
	}

	public void setNotMainBusOkCountBeforeHalfYear(
	        int notMainBusOkCountBeforeHalfYear)
	{
		this.notMainBusOkCountBeforeHalfYear = notMainBusOkCountBeforeHalfYear;
	}

	@Column(name = "not_main_bus_normal_count_before_half_year")
	public int getNotMainBusNormalCountBeforeHalfYear()
	{
		return notMainBusNormalCountBeforeHalfYear;
	}

	public void setNotMainBusNormalCountBeforeHalfYear(
	        int notMainBusNormalCountBeforeHalfYear)
	{
		this.notMainBusNormalCountBeforeHalfYear = notMainBusNormalCountBeforeHalfYear;
	}

	@Column(name = "not_main_bus_bad_count_before_half_year")
	public int getNotMainBusBadCountBeforeHalfYear()
	{
		return notMainBusBadCountBeforeHalfYear;
	}

	public void setNotMainBusBadCountBeforeHalfYear(
	        int notMainBusBadCountBeforeHalfYear)
	{
		this.notMainBusBadCountBeforeHalfYear = notMainBusBadCountBeforeHalfYear;
	}

	@Column(name = "quality_refund_count")
	public int getQualityRefundCount()
	{
		return qualityRefundCount;
	}

	public void setQualityRefundCount(int qualityRefundCount)
	{
		this.qualityRefundCount = qualityRefundCount;
	}

	@Column(name = "no_receive_refund_count")
	public int getNoReceiveRefundCount()
	{
		return noReceiveRefundCount;
	}

	public void setNoReceiveRefundCount(int noReceiveRefundCount)
	{
		this.noReceiveRefundCount = noReceiveRefundCount;
	}

	@Column(name = "no_reason_refund_count")
	public int getNoReasonRefundCount()
	{
		return noReasonRefundCount;
	}

	public void setNoReasonRefundCount(int noReasonRefundCount)
	{
		this.noReasonRefundCount = noReasonRefundCount;
	}

	@Column(name = "after_sale_complaint_count")
	public int getAfterSaleComplaintCount()
	{
		return afterSaleComplaintCount;
	}

	public void setAfterSaleComplaintCount(int afterSaleComplaintCount)
	{
		this.afterSaleComplaintCount = afterSaleComplaintCount;
	}

	@Column(name = "violation_complaint_count")
	public int getViolationComplaintCount()
	{
		return violationComplaintCount;
	}

	public void setViolationComplaintCount(int violationComplaintCount)
	{
		this.violationComplaintCount = violationComplaintCount;
	}

	@Column(name = "false_merch_punish_count")
	public int getFalseMerchPunishCount()
	{
		return falseMerchPunishCount;
	}

	public void setFalseMerchPunishCount(int falseMerchPunishCount)
	{
		this.falseMerchPunishCount = falseMerchPunishCount;
	}

	@Column(name = "infringement_punish_count")
	public int getInfringementPunishCount()
	{
		return infringementPunishCount;
	}

	public void setInfringementPunishCount(int infringementPunishCount)
	{
		this.infringementPunishCount = infringementPunishCount;
	}

	@Column(name = "prohibited_info_punish_count")
	public int getProhibitedInfoPunishCount()
	{
		return prohibitedInfoPunishCount;
	}

	public void setProhibitedInfoPunishCount(int prohibitedInfoPunishCount)
	{
		this.prohibitedInfoPunishCount = prohibitedInfoPunishCount;
	}

	@Column(name = "refund_count")
	public int getRefundCount()
	{
		return refundCount;
	}

	public void setRefundCount(int refundCount)
	{
		this.refundCount = refundCount;
	}

	@Column(name = "complaint_count")
	public int getComplaintCount()
	{
		return complaintCount;
	}

	public void setComplaintCount(int complaintCount)
	{
		this.complaintCount = complaintCount;
	}

}
