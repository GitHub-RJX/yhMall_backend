package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.Transport;

public interface TransportService extends IService<Transport> {
	/**
	 * 插入运费模板和运费项
	 */
	void insertTransportAndTransfee(Transport transport);

	/**
	 * 根据运费模板和运费项
	 */
	void updateTransportAndTransfee(Transport transport);

	/**
	 * 根据id列表删除运费模板和运费项
	 */
	void deleteTransportAndTransfeeAndTranscity(Long[] ids);

	/**
	 * 根据模板id获取运费模板和运费项
	 */
	Transport getTransportAndAllItems(Long transportId);

	/**
	 * 删除缓存
	 */
	void removeTransportAndAllItemsCache(Long transportId);

}
