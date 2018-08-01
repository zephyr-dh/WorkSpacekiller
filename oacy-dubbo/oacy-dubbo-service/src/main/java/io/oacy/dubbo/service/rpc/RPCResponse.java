package io.oacy.dubbo.service.rpc;

import java.io.Serializable;

import lombok.Data;

@Data
public class RPCResponse<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean success = true;// �Ƿ�ɹ�

	private String errorMessage;// ������Ϣ

	private T result;// �����

	private String message;// �Զ�����Ϣ
}
