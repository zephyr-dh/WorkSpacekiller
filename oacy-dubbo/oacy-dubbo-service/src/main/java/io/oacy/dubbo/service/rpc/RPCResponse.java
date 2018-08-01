package io.oacy.dubbo.service.rpc;

import java.io.Serializable;

import lombok.Data;

@Data
public class RPCResponse<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean success = true;// 是否成功

	private String errorMessage;// 错误信息

	private T result;// 结果集

	private String message;// 自定义信息
}
