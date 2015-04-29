package com.shopspider.shopService.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.shopspider.bean.ShopBean;
import com.shopspider.bean.ShopType;
import com.shopspider.common.Consts;
import com.shopspider.http.HttpService;
import com.shopspider.regex.RegexService;
import com.shopspider.shopService.ShopService;

@Service("shopService")
public class ShopServiceimpl implements ShopService
{

	Logger logger = Logger.getLogger(ShopServiceimpl.class);
	@Resource
	RegexService regexService;
	@Resource
	HttpService httpService;

	private static double INVALID_SCORE = -1;
	private static double INVALID_COMPARED_RATE = -101;

	@Override
	public ShopBean parseShopFromList(String shopContent)
	{
		if (shopContent == null || shopContent.equals(""))
		{
			return null;
		}

		ShopBean bean = new ShopBean();

		String shopName = regexService.getSingleGroupItem(shopContent,
		        "<h4>\\s*<a trace=\"shop\"[^>]++>([\\s\\S]*?)</a>");
		if (shopName == null || shopName.equals(""))
		{
			logger.debug("Failed to parse shopName");
		} else
		{
			bean.setShopName(shopName.replaceAll("</?span[^>]*+>", "").trim());
		}

		String shopUrl = regexService.getSingleGroupItem(shopContent,
		        "<h4>\\s*<a trace=\"shop\"[^>]*?href=\"([^\"]++)\"[^>]++>");
		if (shopUrl == null || shopUrl.equals(""))
		{
			logger.debug("Failed to parse shopUrl");
		} else
		{
			bean.setShopUrl(shopUrl.trim());
		}

		String srcShopId = regexService
		        .getSingleGroupItem(shopContent,
		                "<li class=\"list-img\">\\s*<a trace=\"shop\" data-uid=\"([^\"]++)\"");
		if (srcShopId == null || srcShopId.equals(""))
		{
			logger.debug("Failed to parse srcShopId");
		} else
		{
			bean.setSrcShopId(srcShopId.trim());
		}

		String sellerName = regexService
		        .getSingleGroupItem(
		                shopContent,
		                "<span>卖家:</span>\\s*<span class=\"shop-info-list\">\\s*<a trace=\"shop\"[^>]++>([\\s\\S]*?)</a>");
		if (sellerName == null || sellerName.equals(""))
		{
			logger.debug("Failed to parse sellerName");
		} else
		{
			bean.setSellerName(sellerName.trim());
		}

		String location = regexService.getSingleGroupItem(shopContent,
		        "<span class=\"shop-address\">([\\s\\S]*?)</span>");
		if (location != null && !location.equals(""))
		{
			bean.setLocation(location.trim());
		}

		String business = regexService
		        .getSingleGroupItem(
		                shopContent,
		                "<p class=\"main-cat\">\\s*<label>主营:</label>\\s*<a trace=\"shop\"[^>]++>([\\s\\S]*?)</a>");
		if (business == null || business.equals(""))
		{
			logger.debug("Failed to parse bussiness");
		}
		{
			bean.setBusiness(business.replaceAll("</?span[^>]*+>", "").trim());
		}

		String tmallRateUrl = regexService
		        .getSingleGroupItem(
		                shopContent,
		                "<a trace=\"shop\"[^>]*?title=\"天猫\" class=\"mall-icon\"[^>]*?href=\"([^\"]++)\"></a>");

		if (tmallRateUrl != null && !tmallRateUrl.equals(""))
		{
			bean.setShopType(ShopType.Tmall);
			bean.setShopRateUrl(tmallRateUrl);
		} else
		{
			bean.setShopType(ShopType.Taobao);
			String taobaoRateContent = regexService.getSingleGroupItem(
			        shopContent, "<a trace=\"shop\"([^>]++)>卖家信用</a>");
			if (taobaoRateContent == null || taobaoRateContent.equals(""))
			{
				logger.debug("Failed to parse taobaoRateContent");
			} else
			{
				String taobaoRateUrl = regexService.getSingleGroupItem(
				        taobaoRateContent, "href=\"([^\"]++)\"");
				if (taobaoRateUrl == null || taobaoRateUrl.equals(""))
				{
					logger.debug("Failed to parse taobaoRateUrl");
				} else
				{
					bean.setShopRateUrl(taobaoRateUrl);
				}
				String taobaoRateLevel = regexService
				        .getSingleGroupItem(taobaoRateContent,
				                "class=\"rank seller-rank-(\\d++)\"");
				if (taobaoRateLevel != null && !taobaoRateLevel.equals(""))
				{
					bean.setShopSellerCreditLevel(Integer
					        .parseInt(taobaoRateLevel));
				}
			}

		}

		String saleCount = regexService.getSingleGroupItem(shopContent,
		        "<span class=\"info-sale\">销量<em>(\\d++)</em></span>");
		if (saleCount == null || saleCount.equals(""))
		{
			logger.debug("Failed to parse saleCount");
		} else
		{
			bean.setSaleCount(Long.parseLong(saleCount));
		}

		String productCount = regexService.getSingleGroupItem(shopContent,
		        "<span class=\"info-sum\">共<em>(\\d++)</em>件宝贝</span>");
		if (productCount == null || productCount.equals(""))
		{
			logger.debug("Failed to parse totalCount");
		} else
		{
			bean.setProductCount(Integer.parseInt(productCount));
		}

		String newProductCount = regexService.getSingleGroupItem(shopContent,
		        "<a trace=\"shopNewest\"[^>]++>上新<span>(\\d++\\+?)</span></a>");
		if (newProductCount != null && !newProductCount.equals(""))
		{
			if (newProductCount.endsWith("+"))
			{
				// 20+
				bean.setNewProductCount(Consts.NEW_PRODUCT_COUNT_20_PLUS);
			} else
			{
				bean.setNewProductCount(Integer.parseInt(newProductCount));
			}
		}

		// 优惠
		String promotionCount = regexService
		        .getSingleGroupItem(shopContent,
		                "<a trace=\"shopPromotion\"[^>]++>优惠<span>(\\d++\\+?)</span></a>");
		if (promotionCount != null && !promotionCount.equals(""))
		{
			if (promotionCount.endsWith("+"))
			{
				// 20+
				bean.setPromotionCount(Consts.PROMOTION_COUNT_20_PLUS);
			} else
			{
				bean.setPromotionCount(Integer.parseInt(promotionCount));
			}
		}

		String praiseRate = regexService.getSingleGroupItem(shopContent,
		        "<div class=\"good-comt\">好评率:\\s*(\\d+(?:\\.\\d+)?)%</div>");
		if (praiseRate != null && !praiseRate.equals(""))
		{
			bean.setPraiseRate(Double.parseDouble(praiseRate));
		}

		String shopDescriptionContent = regexService
		        .getSingleGroupItem(shopContent,
		                "<div class=\"descr J_descr target-hint-descr\" data-dsr='([^']++)'>");
		if (shopDescriptionContent == null || shopDescriptionContent.equals(""))
		{
			logger.debug("Failed to parse shopDescriptionContent");
		} else
		{
			String descriptionRight = regexService.getSingleGroupItem(
			        shopContent, "mas\":\"(\\d+(?:\\.\\d+)?)\"");
			if (descriptionRight != null && !descriptionRight.equals(""))
			{
				bean.setDescriptionRight(Double.parseDouble(descriptionRight));

			}
			String descriptionRightCompared = regexService.getSingleGroupItem(
			        shopContent, "mg\":\"(-?\\d+(?:\\.\\d+)?)%\"");
			if (descriptionRightCompared != null
			        && !descriptionRightCompared.equals(""))
			{
				bean.setDescriptionRightCompared(Double
				        .parseDouble(descriptionRightCompared));

			}
			String serviceAttitude = regexService.getSingleGroupItem(
			        shopContent, "sas\":\"(\\d+(?:\\.\\d+)?)\"");
			if (serviceAttitude != null && !serviceAttitude.equals(""))
			{
				bean.setServiceAttitude(Double.parseDouble(serviceAttitude));

			}
			String serviceAttitudeCompared = regexService.getSingleGroupItem(
			        shopContent, "sg\":\"(-?\\d+(?:\\.\\d+)?)%\"");
			if (serviceAttitudeCompared != null
			        && !serviceAttitudeCompared.equals(""))
			{
				bean.setServiceAttitudeCompared(Double
				        .parseDouble(serviceAttitudeCompared));

			}
			String deliverySpeed = regexService.getSingleGroupItem(shopContent,
			        "cas\":\"(\\d+(?:\\.\\d+)?)\"");
			if (deliverySpeed != null && !deliverySpeed.equals(""))
			{
				bean.setDeliverySpeed(Double.parseDouble(deliverySpeed));

			}
			String deliverySpeedCompared = regexService.getSingleGroupItem(
			        shopContent, "cg\":\"(-?\\d+(?:\\.\\d+)?)%\"");
			if (deliverySpeedCompared != null
			        && !deliverySpeedCompared.equals(""))
			{
				bean.setDeliverySpeedCompared(Double
				        .parseDouble(deliverySpeedCompared));
			}

			String mainBusiness = regexService.getSingleGroupItem(shopContent,
			        "ind\":\"([^\"]++)\"");
			if (mainBusiness != null && !mainBusiness.equals(""))
			{
				bean.setMainBusiness(mainBusiness);
			}

		}

		String serviceContent = regexService.getSingleGroupItem(shopContent,
		        "<span class=\"icon-list\">([\\s\\S]*?)</span>\\s*</div>");
		if (serviceContent != null && !serviceContent.equals(""))
		{
			List<String> services = regexService.getSingleGroupItems(
			        serviceContent, "<a[^>]++><span>([\\s\\S]*?)</span></a>");
			StringBuilder sb = new StringBuilder();
			for (String service : services)
			{
				sb.append(service + ";");
			}
			if (sb.length() > 0)
			{
				bean.setServices(sb.toString());
			}
		}
		bean.setCreatedAt(new Date());
		return bean;
	}

	@Override
	public ShopBean updateShopDetail(ShopBean shopBean)
	        throws ClientProtocolException, IOException
	{
		shopBean.setUpdatedAt(new Date());
		String rateUrl = shopBean.getShopRateUrl();
		if (rateUrl == null || rateUrl.equals(""))
		{
			logger.info("Shop has no rateUrl :" + shopBean.getShopUrl());
			return shopBean;
		}

		String ratePageContent = httpService.getContent(rateUrl);
		if (ratePageContent == null || ratePageContent.equals(""))
		{
			logger.error("The rate page content is null or empty :"
			        + shopBean.getShopUrl());
		}

		String sellerInfoContent = regexService
		        .getSingleGroupItem(ratePageContent,
		                "<div class=\"hd\">卖家信息</div>([\\s\\S]*?)<div class=\"hd\">店铺服务</div>");
		if (sellerInfoContent == null || sellerInfoContent.equals(""))
		{
			logger.warn("Failed to get seller info content in page:"
			        + shopBean.getShopUrl());
		} else
		{
			String sellerName = regexService.getSingleGroupItem(
			        sellerInfoContent,
			        "<div class=\"title\">\\s*<a[^>]*+>([\\s\\S]*?)</a>");
			if (sellerName == null || sellerName.equals(""))
			{
				logger.warn("shopName is null or empty in page:"
				        + shopBean.getShopUrl());
			} else if (!sellerName.equals(shopBean.getSellerName()))
			{
				logger.warn("ShopName(" + sellerName
				        + ") is different from old(" + shopBean.getShopName()
				        + ")");
				shopBean.setSellerName(sellerName);
			}

			String shopTypeStr = regexService.getSingleGroupItem(
			        sellerInfoContent, "(<div class=\"tmall-pro\">)");
			ShopType type = ShopType.Taobao;
			if (shopTypeStr != null && !shopTypeStr.equals(""))
			{
				type = ShopType.Tmall;
			}
			if (!type.equals(shopBean.getShopType()))
			{
				logger.warn("ShopType(" + type + ") is different from old("
				        + shopBean.getShopType() + ")");
				shopBean.setShopType(type);
			}

			String mainBusiness = regexService.getSingleGroupItem(
			        sellerInfoContent, "当前主营：<a[^>]*+>([\\s\\S]*?)</a>");
			if (mainBusiness == null || mainBusiness.equals(""))
			{
				logger.warn("mainBusiness is null or empty in page:"
				        + shopBean.getShopUrl());
			} else
			{
				mainBusiness = removeHtmlCharacters(mainBusiness).trim();
				if (!mainBusiness.equals(shopBean.getMainBusiness()))
				{
					logger.warn("mainBusiness(" + mainBusiness
					        + ") is different from old("
					        + shopBean.getMainBusiness() + ")");
					shopBean.setMainBusiness(mainBusiness);
				}
			}

			// 这里抓到的不带省份
			// String location = regexService.getSingleGroupItem(
			// sellerInfoContent, "所在地区：\\s*([\\s\\S]*?)\\s*</li>");
			// if (location == null || location.equals(""))
			// {
			// logger.warn("location is null or empty in page:"
			// + shopBean.getShopUrl());
			// } else if (!location.equals(shopBean.getLocation()))
			// {
			// logger.warn("location(" + location + ") is different from old("
			// + shopBean.getLocation() + ")");
			// shopBean.setLocation(location);
			// }

			if (type.equals(ShopType.Taobao))
			{
				// 淘宝
				String establishAt = regexService
				        .getSingleGroupItem(
				                ratePageContent,
				                "<input type=\"hidden\" name=\"shopStartDate\" id=\"J_showShopStartDate\" value=\"([^\"]++)\" />");
				if (establishAt == null || establishAt.equals(""))
				{
					logger.warn("establishAt is null or empty in page : "
					        + shopBean.getShopUrl());
				} else
				{
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					try
					{
						shopBean.setEstablishAt(format.parse(establishAt));
					} catch (ParseException e)
					{
						logger.warn("Catch exception(" + e.getMessage()
						        + ") when pare establish date(" + establishAt
						        + ") in page:" + shopBean.getShopUrl());
					}
				}

				String sellerCreditValueStr = regexService.getSingleGroupItem(
				        sellerInfoContent, "卖家信用：\\s*(\\d++)");
				if (sellerCreditValueStr == null
				        || sellerCreditValueStr.equals(""))
				{
					logger.warn("sellerCreditValueStr is null or empty in page"
					        + shopBean.getShopUrl());
				} else
				{
					shopBean.setShopSellerCreditValue(Integer
					        .parseInt(sellerCreditValueStr));
				}

				String sellerCreditValuePic = regexService.getSingleGroupItem(
				        sellerInfoContent,
				        "卖家信用：[^<]*+<a[^>]*+><img src=\"([^\"]++)\"");
				if (sellerCreditValuePic == null
				        || sellerCreditValuePic.equals(""))
				{
					logger.warn("sellerCreditValuePic is null or empty in page"
					        + shopBean.getShopUrl());
				} else
				{
					shopBean.setShopSellerCreditPic(sellerCreditValuePic);
				}

				String buyerCreditValueStr = regexService.getSingleGroupItem(
				        sellerInfoContent, "买家信用：\\s*(\\d++)");
				if (buyerCreditValueStr == null
				        || buyerCreditValueStr.equals(""))
				{
					logger.warn("buyerCreditValueStr is null or empty in page"
					        + shopBean.getShopUrl());
				} else
				{
					shopBean.setShopBuyerCreditValue(Integer
					        .parseInt(buyerCreditValueStr));
				}

				String buyerCreditValuePic = regexService.getSingleGroupItem(
				        sellerInfoContent,
				        "买家信用：[^<]*+<a[^>]*+><img src=\"([^\"]++)\"");
				if (buyerCreditValuePic == null
				        || buyerCreditValuePic.equals(""))
				{
					logger.debug("buyerCreditValuePic is null or empty in page"
					        + shopBean.getShopUrl());
				} else
				{
					shopBean.setShopBuyerCreditPic(buyerCreditValuePic);
				}

			} else if (type.equals(ShopType.Tmall))
			{
				String companyName = regexService.getSingleGroupItem(
				        sellerInfoContent,
				        "公司名称：</div><div[^>]*+>([\\s\\S]*?)</div>");
				if (companyName == null || companyName.equals(""))
				{
					logger.warn("companyName is null or empty in page"
					        + shopBean.getShopUrl());
				} else
				{
					companyName = removeHtmlCharacters(companyName);
					shopBean.setCompanyName(companyName);
				}

			}

		}

		if (shopBean.getShopType().equals(ShopType.Taobao))
		{
			String servicesContent = regexService.getSingleGroupItem(
			        ratePageContent,
			        "卖家已向消费者承诺：</div>\\s*<ul>([\\s\\S]*?)</ul>");
			if (servicesContent == null || servicesContent.equals(""))
			{
				logger.warn("servicesContent is null or empty in page"
				        + shopBean.getShopUrl());
			} else
			{
				List<String> services = regexService.getSingleGroupItems(
				        servicesContent, "<a[^>]*+>([\\s\\S]*?)</a>");
				if (services == null)
				{
					logger.warn("services is null or empty in page"
					        + shopBean.getShopUrl());
				} else
				{
					StringBuilder sb = new StringBuilder();
					for (String service : services)
					{
						service = service.replaceAll(
						        "<span>([\\s\\S]*?)</span>", "").trim();
						sb.append(service + ";");
					}
					shopBean.setServices(sb.toString());
				}
			}
		} else if (shopBean.getShopType().equals(ShopType.Tmall))
		{
			String servicesContent = regexService.getSingleGroupItem(
			        ratePageContent,
			        "<div class=\"promise\">([\\s\\S]*?)</div>");
			if (servicesContent == null || servicesContent.equals(""))
			{
				logger.warn("servicesContent is null or empty in page"
				        + shopBean.getShopUrl());
			} else
			{
				List<String> services = regexService.getSingleGroupItems(
				        servicesContent, "<a[^>]*?title=\"([^\"]++)\"");
				if (services == null)
				{
					logger.warn("services is null or empty in page"
					        + shopBean.getShopUrl());
				} else
				{
					StringBuilder sb = new StringBuilder();
					for (String service : services)
					{
						sb.append(service + ";");
					}
					shopBean.setServices(sb.toString());
				}
			}
		}

		String chargeBalanceStr = regexService
		        .getSingleGroupItem(ratePageContent,
		                "<div class=\"charge\">卖家当前保证金余额 <span>.??([\\d,]++(?:\\.\\d++)?)</span>");
		if (chargeBalanceStr == null || chargeBalanceStr.equals(""))
		{
			logger.warn("chrgeBalanceStr is null or empty in page"
			        + shopBean.getShopUrl());
		} else
		{
			chargeBalanceStr = chargeBalanceStr.replaceAll(",", "").trim();
			shopBean.setChrgeBalance(Double.parseDouble(chargeBalanceStr));
		}

		// 店铺半年内动态评分
		String scoreContent = regexService.getSingleGroupItem(ratePageContent,
		        "<h4[^>]*+>店铺半年内动态评分</h4>([\\s\\S]*?</ul>\\s*</div>)");
		if (scoreContent == null || scoreContent.equals(""))
		{
			logger.warn("scoreContent is null or empty in page"
			        + shopBean.getShopUrl());
		} else
		{
			parseScoreContent(scoreContent, shopBean);

		}

		parseServiceInfo(ratePageContent, shopBean);
		// http://rate.taobao.com/ShopService4C.htm?userNumId=853982&ua=169WsMPac%2FFS4KgNn%2BYfhzduo4U2NC0zh9cAS4%3D|WUCLjKhqo8L2bKKqWZsmRRg%3D|WMEKRlV%2B3D9a6XWaidNWNQKTWH0bX%2BkuQX3gxQPg|X0YLbX78NUR3bqPEsHLPvo4XWL5tF6rF9W%2BjhACaVzQBmldfa8EMTxI%3D|XkdILogCr87%2BZq2qnsRBluT7IA59yQDP5vhmIVZcLpc%3D|XccL7V4kierG3xIVpjTliNU%3D
		// String serviceInfoContent = regexService.getSingleGroup(
		// ratePageContent,
		// "<h4[^>]*+>店铺30天内服务情况</h4>([\\s\\S]*?)</script>");
		// if (serviceInfoContent == null || serviceInfoContent.equals(""))
		// {
		// logger.warn("serviceInfoContent is null or empty in page"
		// + shopBean.getShopUrl());
		// } else
		// {
		//
		// }

		if (shopBean.getShopType().equals(ShopType.Taobao))
		{
			String creditInfoContent = regexService.getSingleGroupItem(
			        ratePageContent,
			        "<h4[^>]*+>卖家信用评价展示([\\s\\S]*?</table>\\s*</li>\\s*</ul>)");
			if (creditInfoContent == null || creditInfoContent.equals(""))
			{
				logger.warn("creditInfoContent is null or empty in page"
				        + shopBean.getShopUrl());
			} else
			{
				String praiseRateStr = regexService.getSingleGroupItem(
				        creditInfoContent, "好评率：(\\d+(?:\\.\\d+)?)%");
				if (praiseRateStr != null && !praiseRateStr.equals(""))
				{
					shopBean.setPraiseRate(Double.parseDouble(praiseRateStr));
				}

				parseCreditInfo(creditInfoContent, shopBean);

			}

			String creditStructContent = regexService
			        .getSingleGroupItem(ratePageContent,
			                "<h4[^>]*+>卖家历史信用构成</h4>([\\s\\S]*?主营占比：(?:\\d+(?:\\.\\d+)?)%</div>)");
			if (creditStructContent == null || creditStructContent.equals(""))
			{
				logger.warn("creditStructContent is null or empty in page"
				        + shopBean.getShopUrl());
			} else
			{
				String mainBusinessCredicValueStr = regexService
				        .getSingleGroupItem(creditStructContent,
				                "主营行业：[\\s\\S]*?&nbsp;&nbsp;(\\d++)</div>");
				if (mainBusinessCredicValueStr != null
				        && !mainBusinessCredicValueStr.equals(""))
				{
					shopBean.setMainBusinessCreditValue(Integer
					        .parseInt(mainBusinessCredicValueStr));
				}

				String mainBusinessRateStr = regexService.getSingleGroupItem(
				        creditStructContent, "主营占比：(\\d+(?:\\.\\d+)?)%");
				if (mainBusinessRateStr != null
				        && !mainBusinessRateStr.equals(""))
				{
					shopBean.setMainBusinessRate(Double
					        .parseDouble(mainBusinessRateStr));
				}
			}
		}

		return shopBean;

	}

	private void parseServiceInfo(String content, ShopBean shopBean)
	        throws ClientProtocolException, IOException
	{
		// http://rate.taobao.com/ShopService4C.htm?userNumId=853982&ua=169WsMPac%2FFS4KgNn%2BYfhzduo4U2NC0zh9cAS4%3D|WUCLjKhqo8L2bKKqWZsmRRg%3D|WMEKRlV%2B3D9a6XWaidNWNQKTWH0bX%2BkuQX3gxQPg|X0YLbX78NUR3bqPEsHLPvo4XWL5tF6rF9W%2BjhACaVzQBmldfa8EMTxI%3D|XkdILogCr87%2BZq2qnsRBluT7IA59yQDP5vhmIVZcLpc%3D|XccL7V4kierG3xIVpjTliNU%3D
		String sourceShopId = regexService.getSingleGroupItem(
		        shopBean.getShopRateUrl(), "user-rate-(\\d++)");
		if (sourceShopId == null || sourceShopId.equals(""))
		{
			logger.warn("sourceShopId is null or empty in page : "
			        + shopBean.getShopUrl());
			return;
		}

		String serviceContent = httpService
		        .getContent("http://rate.taobao.com/ShopService4C.htm?userNumId="
		                + sourceShopId);
		if (serviceContent == null || serviceContent.equals(""))
		{
			logger.warn("serviceContent is null or empty in page : "
			        + shopBean.getShopUrl());
			return;
		}

		JSONObject dataJson = JSONObject.fromObject(serviceContent);
		if (dataJson.isNullObject())
		{
			return;
		}
		JSONObject avgRefundJson = dataJson.getJSONObject(("avgRefund"));
		if (!avgRefundJson.isNullObject())
		{
			shopBean.setAverageRefundSpeed(avgRefundJson.getDouble("localVal"));
			shopBean.setAverageRefundSpeedOfBussiness(avgRefundJson
			        .getDouble("indVal"));

		}

		JSONObject ratRefundJson = dataJson.getJSONObject(("ratRefund"));
		if (!ratRefundJson.isNullObject())
		{
			shopBean.setRefundRate(ratRefundJson.getDouble("localVal"));
			shopBean.setRefundRateOfBussiness(ratRefundJson.getDouble("indVal"));

			shopBean.setQualityRefundCount(ratRefundJson
			        .getInt("merchQualityTimes"));
			shopBean.setNoReceiveRefundCount(ratRefundJson
			        .getInt("merchReceiveTimes"));
			shopBean.setNoReasonRefundCount(ratRefundJson
			        .getInt("noReasonTimes"));
			shopBean.setRefundCount(ratRefundJson.getInt("refundCount"));
		}

		JSONObject complaintsJson = dataJson.getJSONObject(("complaints"));
		if (!complaintsJson.isNullObject())
		{
			shopBean.setComplaintRate(complaintsJson.getDouble("localVal"));
			shopBean.setComplaintRateOfBussiness(complaintsJson
			        .getDouble("indVal"));

			shopBean.setComplaintCount(complaintsJson.getInt("complaintsCount"));
			shopBean.setAfterSaleComplaintCount(complaintsJson
			        .getInt("afterSaleTimes"));
			shopBean.setViolationComplaintCount(complaintsJson
			        .getInt("violationTimes"));

		}

		JSONObject punishJson = dataJson.getJSONObject(("punish"));
		if (!punishJson.isNullObject())
		{
			shopBean.setPunishCount(punishJson.getDouble("localVal"));
			shopBean.setPunishCountOfBussiness(punishJson.getDouble("indVal"));

			shopBean.setPunishCount(punishJson.getInt("punishCount"));
			shopBean.setFalseMerchPunishCount(punishJson
			        .getInt("falseMerchTimes"));
			shopBean.setInfringementPunishCount(punishJson
			        .getInt("infringementTimes"));
			shopBean.setProhibitedInfoPunishCount(punishJson
			        .getInt("prohibitedInfoTimes"));

		}

	}

	private void parseCreditInfo(String content, ShopBean shopBean)
	{
		// 半年以前
		List<String> tables = regexService.getSingleGroupItems(content,
		        "<li[^>]*+>\\s*<table>([\\s\\S]*?)</table>\\s*</li>");
		if (tables == null || tables.size() < 4)
		{
			logger.info("tables is null or size is smaller than 4 in page :"
			        + shopBean.getShopUrl());
		} else
		{
			String oneMonthContent = tables.get(1);

			String totalOneMonthContent = regexService.getSingleGroupItem(
			        oneMonthContent, "<td>总数</td>([\\s\\S]*?)</tr>");
			if (totalOneMonthContent != null
			        && !totalOneMonthContent.equals(""))
			{
				List<String> nums = regexService.getSingleGroupItems(
				        totalOneMonthContent, "<a[^>]*+>\\s*(\\d++)\\s*</a>");
				if (nums != null && nums.size() == 3)
				{
					shopBean.setTotalOkCountOneMonth(Integer.parseInt(nums
					        .get(0)));
					shopBean.setTotalNormalCountOneMonth(Integer.parseInt(nums
					        .get(1)));
					shopBean.setTotalBadCountOneMonth(Integer.parseInt(nums
					        .get(2)));

				}
			}

			String mainBusMonthContent = regexService.getSingleGroupItem(
			        oneMonthContent, "<td class=\"orange\">([\\s\\S]*?)</tr>");
			if (mainBusMonthContent != null && !mainBusMonthContent.equals(""))
			{
				List<String> nums = regexService.getSingleGroupItems(
				        mainBusMonthContent, "<td>\\s*(\\d++)\\s*</td>");
				if (nums != null && nums.size() == 3)
				{
					shopBean.setMainBusOkCountOneMonth(Integer.parseInt(nums
					        .get(0)));
					shopBean.setMainBusNormalCountOneMonth(Integer
					        .parseInt(nums.get(1)));
					shopBean.setMainBusBadCountOneMonth(Integer.parseInt(nums
					        .get(2)));

				}
			}

			String notMainBusMonthContent = regexService.getSingleGroupItem(
			        oneMonthContent, "<td>非主营行业</td>([\\s\\S]*?)</tr>");
			if (notMainBusMonthContent != null
			        && !notMainBusMonthContent.equals(""))
			{
				List<String> nums = regexService.getSingleGroupItems(
				        notMainBusMonthContent, "<td>\\s*(\\d++)\\s*</td>");
				if (nums != null && nums.size() == 3)
				{
					shopBean.setNotMainBusOkCountOneMonth(Integer.parseInt(nums
					        .get(0)));
					shopBean.setNotMainBusNormalCountOneMonth(Integer
					        .parseInt(nums.get(1)));
					shopBean.setNotMainBusBadCountOneMonth(Integer
					        .parseInt(nums.get(2)));

				}
			}

			String halfYearContent = tables.get(2);

			String totalInHalfYearContent = regexService.getSingleGroupItem(
			        halfYearContent, "<td>总数</td>([\\s\\S]*?)</tr>");
			if (totalInHalfYearContent != null
			        && !totalInHalfYearContent.equals(""))
			{
				List<String> nums = regexService.getSingleGroupItems(
				        totalInHalfYearContent, "<a[^>]*+>\\s*(\\d++)\\s*</a>");
				if (nums != null && nums.size() == 3)
				{
					shopBean.setTotalOkCountInHalfYear(Integer.parseInt(nums
					        .get(0)));
					shopBean.setTotalNormalCountInHalfYear(Integer
					        .parseInt(nums.get(1)));
					shopBean.setTotalBadCountInHalfYear(Integer.parseInt(nums
					        .get(2)));

				}
			}

			String mainBusInHalfYearContent = regexService.getSingleGroupItem(
			        halfYearContent, "<td class=\"orange\">([\\s\\S]*?)</tr>");
			if (mainBusInHalfYearContent != null
			        && !mainBusInHalfYearContent.equals(""))
			{
				List<String> nums = regexService.getSingleGroupItems(
				        mainBusInHalfYearContent, "<td>\\s*(\\d++)\\s*</td>");
				if (nums != null && nums.size() == 3)
				{
					shopBean.setMainBusOkCountInHalfYear(Integer.parseInt(nums
					        .get(0)));
					shopBean.setMainBusNormalCountInHalfYear(Integer
					        .parseInt(nums.get(1)));
					shopBean.setMainBusBadCountInHalfYear(Integer.parseInt(nums
					        .get(2)));

				}
			}

			String notMainBusInHalfYearContent = regexService
			        .getSingleGroupItem(halfYearContent,
			                "<td>非主营行业</td>([\\s\\S]*?)</tr>");
			if (notMainBusInHalfYearContent != null
			        && !notMainBusInHalfYearContent.equals(""))
			{
				List<String> nums = regexService
				        .getSingleGroupItems(notMainBusInHalfYearContent,
				                "<td>\\s*(\\d++)\\s*</td>");
				if (nums != null && nums.size() == 3)
				{
					shopBean.setNotMainBusOkCountInHalfYear(Integer
					        .parseInt(nums.get(0)));
					shopBean.setNotMainBusNormalCountInHalfYear(Integer
					        .parseInt(nums.get(1)));
					shopBean.setNotMainBusBadCountInHalfYear(Integer
					        .parseInt(nums.get(2)));

				}
			}

			String beforeHalfYearContent = tables.get(3);

			String totalBeforeHalfYearContent = regexService
			        .getSingleGroupItem(beforeHalfYearContent,
			                "<td>总数</td>([\\s\\S]*?)</tr>");
			if (totalBeforeHalfYearContent != null
			        && !totalBeforeHalfYearContent.equals(""))
			{
				List<String> nums = regexService.getSingleGroupItems(
				        totalBeforeHalfYearContent,
				        "<a[^>]*+>\\s*(\\d++)\\s*</a>");
				if (nums != null && nums.size() == 3)
				{
					shopBean.setTotalOkCountBeforeHalfYear(Integer
					        .parseInt(nums.get(0)));
					shopBean.setTotalNormalCountBeforeHalfYear(Integer
					        .parseInt(nums.get(1)));
					shopBean.setTotalBadCountBeforeHalfYear(Integer
					        .parseInt(nums.get(2)));

				}
			}

		}

	}

	/**
	 * 店铺半年内动态评分
	 * 
	 * @param scoreContent
	 * @param shopBean
	 */
	private void parseScoreContent(String scoreContent, ShopBean shopBean)
	{
		String descriptionRightContent = regexService
		        .getSingleGroupItem(scoreContent,
		                "<span class=\"title\">宝贝与描述相符：</span>([\\s\\S]*?)</div>\\s*</div>\\s*</li>");
		if (descriptionRightContent == null
		        || descriptionRightContent.equals(""))
		{
			logger.warn("descriptionRightContent is null or empty in page: "
			        + shopBean.getShopUrl());
		} else
		{
			String countStr = regexService.getSingleGroupItem(
			        descriptionRightContent, "共<span>(\\d++)</span>人");
			if (countStr == null || countStr.equals(""))
			{
				logger.warn("countStr is null or empty in page: "
				        + shopBean.getShopUrl());
			} else
			{
				shopBean.setDescriptionRightScoreCount(Integer
				        .parseInt(countStr));
			}

			List<Double> scores = parseSingleScoreItem(descriptionRightContent);

			if (scores.get(0) != INVALID_SCORE)
			{
				shopBean.setDescriptionRight(scores.get(0));
			}
			if (scores.get(1) != INVALID_SCORE)
			{
				shopBean.setDescriptionRight1Rate(scores.get(1));
			}
			if (scores.get(2) != INVALID_SCORE)
			{
				shopBean.setDescriptionRight2Rate(scores.get(2));
			}
			if (scores.get(3) != INVALID_SCORE)
			{
				shopBean.setDescriptionRight3Rate(scores.get(3));
			}
			if (scores.get(4) != INVALID_SCORE)
			{
				shopBean.setDescriptionRight4Rate(scores.get(4));
			}
			if (scores.get(5) != INVALID_SCORE)
			{
				shopBean.setDescriptionRight5Rate(scores.get(5));
			}
			if (scores.get(6) != INVALID_COMPARED_RATE)
			{
				shopBean.setDescriptionRightCompared(scores.get(6));
			}
		}

		String attitudeContent = regexService
		        .getSingleGroupItem(scoreContent,
		                "<span class=\"title\">卖家的服务态度：</span>([\\s\\S]*?)</div>\\s*</div>\\s*</li>");
		if (attitudeContent == null || attitudeContent.equals(""))
		{
			logger.warn("attitudeContent is null or empty in page: "
			        + shopBean.getShopUrl());
		} else
		{
			String countStr = regexService.getSingleGroupItem(attitudeContent,
			        "共<span>(\\d++)</span>人");
			if (countStr == null || countStr.equals(""))
			{
				logger.warn("countStr is null or empty in page: "
				        + shopBean.getShopUrl());
			} else
			{
				shopBean.setServiceAttitudeScoreCount(Integer
				        .parseInt(countStr));
			}

			List<Double> scores = parseSingleScoreItem(attitudeContent);

			if (scores.get(0) != INVALID_SCORE)
			{
				shopBean.setServiceAttitude(scores.get(0));
			}
			if (scores.get(1) != INVALID_SCORE)
			{
				shopBean.setServiceAttitude1Rate(scores.get(1));
			}
			if (scores.get(2) != INVALID_SCORE)
			{
				shopBean.setServiceAttitude2Rate(scores.get(2));
			}
			if (scores.get(3) != INVALID_SCORE)
			{
				shopBean.setServiceAttitude3Rate(scores.get(3));
			}
			if (scores.get(4) != INVALID_SCORE)
			{
				shopBean.setServiceAttitude4Rate(scores.get(4));
			}
			if (scores.get(5) != INVALID_SCORE)
			{
				shopBean.setServiceAttitude5Rate(scores.get(5));
			}
			if (scores.get(6) != INVALID_COMPARED_RATE)
			{
				shopBean.setServiceAttitudeCompared(scores.get(6));
			}
		}

		String deliverySpeedContent = regexService
		        .getSingleGroupItem(scoreContent,
		                "<span class=\"title\">卖家发货的速度：</span>([\\s\\S]*?)</div>\\s*</div>\\s*</li>");
		if (deliverySpeedContent == null || deliverySpeedContent.equals(""))
		{
			logger.warn("deliverySpeedContent is null or empty in page: "
			        + shopBean.getShopUrl());
		} else
		{
			String countStr = regexService.getSingleGroupItem(
			        deliverySpeedContent, "共<span>(\\d++)</span>人");
			if (countStr == null || countStr.equals(""))
			{
				logger.warn("countStr is null or empty in page: "
				        + shopBean.getShopUrl());
			} else
			{
				shopBean.setDeliverySpeedScoreCount(Integer.parseInt(countStr));
			}

			List<Double> scores = parseSingleScoreItem(deliverySpeedContent);

			if (scores.get(0) != INVALID_SCORE)
			{
				shopBean.setDeliverySpeed(scores.get(0));
			}
			if (scores.get(1) != INVALID_SCORE)
			{
				shopBean.setDeliverySpeed1Rate(scores.get(1));
			}
			if (scores.get(2) != INVALID_SCORE)
			{
				shopBean.setDeliverySpeed2Rate(scores.get(2));
			}
			if (scores.get(3) != INVALID_SCORE)
			{
				shopBean.setDeliverySpeed3Rate(scores.get(3));
			}
			if (scores.get(4) != INVALID_SCORE)
			{
				shopBean.setDeliverySpeed4Rate(scores.get(4));
			}
			if (scores.get(5) != INVALID_SCORE)
			{
				shopBean.setDeliverySpeed5Rate(scores.get(5));
			}
			if (scores.get(6) != INVALID_COMPARED_RATE)
			{
				shopBean.setDeliverySpeedCompared(scores.get(6));
			}
		}
	}

	/**
	 * 0:总平均分 1:1分占比 2:2分占比 3:3分占比 4:4分占比 5:5分占比 6:与同业相比
	 * 
	 * @param descriptionRightContent
	 * @return
	 */
	private List<Double> parseSingleScoreItem(String content)
	{
		List<Double> result = new ArrayList<Double>(5);
		String averageScoreStr = regexService
		        .getSingleGroupItem(content,
		                "<em title=\"(\\d+(?:\\.\\d+)?)分\"[^>]*+>(?:\\d+(?:\\.\\d+)?)</em>分\\s*共");
		if (averageScoreStr == null || averageScoreStr.equals(""))
		{
			result.add(INVALID_SCORE);
		} else
		{
			result.add(Double.parseDouble(averageScoreStr));
		}

		String scoreRate1Str = regexService
		        .getSingleGroupItem(
		                content,
		                " <span class=\"small-star-no1\"></span>\\s*<span[^>]*+></span>\\s*<em[^>]*+>(\\d+(?:\\.\\d+)?)%</em>");
		if (scoreRate1Str == null || scoreRate1Str.equals(""))
		{
			scoreRate1Str = regexService.getSingleGroupItem(content,
			        " <span class=\"small-star-no1\"></span>\\s*(暂无人打分)");
			if (scoreRate1Str != null && !scoreRate1Str.equals(""))
			{
				result.add(0.0);
			} else
			{
				result.add(INVALID_SCORE);
			}
		} else
		{
			result.add(Double.parseDouble(scoreRate1Str));
		}

		String scoreRate2Str = regexService
		        .getSingleGroupItem(
		                content,
		                " <span class=\"small-star-no2\"></span>\\s*<span[^>]*+></span>\\s*<em[^>]*+>(\\d+(?:\\.\\d+)?)%</em>");
		if (scoreRate2Str == null || scoreRate2Str.equals(""))
		{
			scoreRate2Str = regexService.getSingleGroupItem(content,
			        " <span class=\"small-star-no2\"></span>\\s*(暂无人打分)");
			if (scoreRate2Str != null && !scoreRate2Str.equals(""))
			{
				result.add(0.0);
			} else
			{
				result.add(INVALID_SCORE);
			}
		} else
		{
			result.add(Double.parseDouble(scoreRate2Str));
		}

		String scoreRate3Str = regexService
		        .getSingleGroupItem(
		                content,
		                " <span class=\"small-star-no3\"></span>\\s*<span[^>]*+></span>\\s*<em[^>]*+>(\\d+(?:\\.\\d+)?)%</em>");
		if (scoreRate3Str == null || scoreRate3Str.equals(""))
		{
			scoreRate3Str = regexService.getSingleGroupItem(content,
			        " <span class=\"small-star-no3\"></span>\\s*(暂无人打分)");
			if (scoreRate3Str != null && !scoreRate3Str.equals(""))
			{
				result.add(0.0);
			} else
			{
				result.add(INVALID_SCORE);
			}
		} else
		{
			result.add(Double.parseDouble(scoreRate3Str));
		}

		String scoreRate4Str = regexService
		        .getSingleGroupItem(
		                content,
		                " <span class=\"small-star-no4\"></span>\\s*<span[^>]*+></span>\\s*<em[^>]*+>(\\d+(?:\\.\\d+)?)%</em>");
		if (scoreRate4Str == null || scoreRate4Str.equals(""))
		{
			scoreRate4Str = regexService.getSingleGroupItem(content,
			        " <span class=\"small-star-no4\"></span>\\s*(暂无人打分)");
			if (scoreRate4Str != null && !scoreRate4Str.equals(""))
			{
				result.add(0.0);
			} else
			{
				result.add(INVALID_SCORE);
			}
		} else
		{
			result.add(Double.parseDouble(scoreRate4Str));
		}

		String scoreRate5Str = regexService
		        .getSingleGroupItem(
		                content,
		                " <span class=\"small-star-no5\"></span>\\s*<span[^>]*+></span>\\s*<em[^>]*+>(\\d+(?:\\.\\d+)?)%</em>");
		if (scoreRate5Str == null || scoreRate5Str.equals(""))
		{
			scoreRate5Str = regexService.getSingleGroupItem(content,
			        " <span class=\"small-star-no5\"></span>\\s*(暂无人打分)");
			if (scoreRate5Str != null && !scoreRate5Str.equals(""))
			{
				result.add(0.0);
			} else
			{
				result.add(INVALID_SCORE);
			}
		} else
		{
			result.add(Double.parseDouble(scoreRate5Str));
		}

		List<String> comparedScoreStrs = regexService
		        .getMultiGroupItems(
		                content,
		                "<strong class=\"percent (normal|lower|over)\">(-*|\\d+(?:\\.\\d+)?)%?</strong>");
		if (comparedScoreStrs == null || comparedScoreStrs.size() != 2)
		{
			logger.info("comparedScoreStrs is null or size is not 2");
			result.add(INVALID_COMPARED_RATE);
		} else
		{
			String flag = comparedScoreStrs.get(0);
			String value = comparedScoreStrs.get(1);
			if (flag.equals("normal"))
			{
				result.add(0.0);
			} else if (flag.equals("lower"))
			{
				result.add(-1 * Double.parseDouble(value));
			} else if (flag.equals("over"))
			{
				result.add(Double.parseDouble(value));
			} else
			{
				result.add(INVALID_COMPARED_RATE);
			}
		}
		return result;
	}

	private String removeHtmlCharacters(String content)
	{
		return content.replaceAll("&nbsp;", " ");
	}
}
