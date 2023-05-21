//
///**
// * DAO
// *
// * @author Frank Cheung sp42@qq.com
// *
// */
//public interface WfDao {
//	interface ProcessDao extends IDataService<ProcessPO> {
//		@Select("SELECT max(version) FROM ${tableName} WHERE name = ?")
//		@KeyOfMapParams("name")
//		Integer getLatestProcessVersion(String name);
//	}
//
//	public static final ProcessDao ProcessDAO = new Caller("cms", "wf_process").bind(ProcessDao.class, ProcessPO.class);
//


//
//
//	interface OrderHistoryDao extends IDataService<OrderHistory> {
//		@Select("SELECT * FROM ${tableName} WHERE orderId = ?")
//		public OrderHistory findByOrderId(Long orderId);
//	}
//
//	public static final OrderHistoryDao OrderHistoryDAO = new Caller("cms", "wf_order_history").bind(OrderHistoryDao.class, OrderHistory.class);
//

//}
