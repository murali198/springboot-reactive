package com.murali.product.exception;

public class ProductException extends RuntimeException {

    private IResponseCode errorCode;
    private String[] errorParams;
    private Throwable cause;
    private String message;

    public ProductException() {}

    /**
     * Constructs an <code>ProductException</code> with the
     * the specified code.
     *
     * @param  errorCode the error code (which is saved for later retrieval
     *         by the {@link #getErrorCode()} method).
     */
    public ProductException(final IResponseCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Constructs a ProductException exception with the specified code and parameter.
     *
     * @param  code the error code (which is saved for later retrieval
     *         by the {@link #getErrorCode()} method).
     *
     * @param  param the error parameter (which is saved for later retrieval
     *         by the {@link #getErrorParams()} method).
     */
    public ProductException(final IResponseCode errorCode, final Object params) {
        this.errorCode = errorCode;
        if(params instanceof String[]) {
            errorParams = (String[])params;
        } else if(params instanceof String) {
            errorParams = new String[]{(String)params};
        } else {
            final String errorParam = params != null ? params.toString() : "";
            errorParams = new String[]{errorParam};
        }
    }


    /**
     * Constructs a ProductException exception with the specified code, parameter and
     * cause.
     *
     * <p>Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this exception's detail
     * message.
     *
     * @param  code the error code (which is saved for later retrieval
     *         by the {@link #getErrorCode()} method).
     *
     * @param  param the error parameter (which is saved for later retrieval
     *         by the {@link #getErrorParams()} method).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value
     *         is permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public ProductException(final IResponseCode errorCode, final Object params, final Throwable cause) {
        this(errorCode,params);
        this.cause = cause;
    }

    /**
     * Constructs a ProductException with a target exception
     * and a errorCode.
     *
     * @param  code the error code (which is saved for later retrieval
     *         by the {@link #getErrorCode()} method).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).
     */
    public ProductException(final IResponseCode errorCode, final Throwable cause) {
        this.errorCode = errorCode;
        this.cause = cause;
    }

    /**
     * Returns the internal errorCode of this exception or <code>null</code> if the
     * errorCode is nonexistent or unknown.
     *
     * <p>This implementation returns the errorCode that was supplied via one of the constructors
     * requiring a <tt>ProductException</tt>.
     *
     * @return  the errorCode of this exception or <code>null</code> if the
     *          errorCode is nonexistent or unknown.
     */
    public String getInternalCode() {
        return errorCode != null ? errorCode.getInternalCode() : null;
    }

    /**
     * Returns the external errorCode of this exception or <code>null</code> if the
     * errorCode is nonexistent or unknown.
     *
     * <p>This implementation returns the errorCode that was supplied via one of the constructors
     * requiring a <tt>ProductException</tt>.
     *
     * @return  the errorCode of this exception or <code>null</code> if the
     *          errorCode is nonexistent or unknown.
     */
    public String getExternalCode() {
        return errorCode != null ? errorCode.getExternalCode() : null;
    }

    public String getGroupCode() {
        return errorCode != null ? errorCode.getGroupCode() : null;
    }

    /**
     * Returns the errorParams of this exception or <code>null</code> if the
     * errorCode is nonexistent or unknown.
     *
     * <p>This implementation returns the errorParams that was supplied via one of the constructors
     * requiring a <tt>ProductException</tt>.
     *
     * @return  the errorParams of this exception or <code>null</code> if the
     *          errorParams is nonexistent or unknown.
     */
    public String[] getErrorParams() {
        return errorParams;
    }

    /**
     * Returns the cause of this throwable or <code>null</code> if the
     * cause is nonexistent or unknown.
     *
     * <p>This implementation returns the cause that was supplied via one of the constructors
     * requiring a <tt>ProductException</tt>.
     *
     * @return  the cause of this exception or <code>null</code> if the
     *          cause is nonexistent or unknown.
     */
    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    /**
     * Returns the detail message string of this ProductException.
     *
     * @return  the detail message string of this <tt>ProductException</tt> instance
     *          (which may be <tt>null</tt>).
     */
    public String getMessage() {
        String errorMsg = null;
        if((message == null)  && (errorCode != null)) {
            errorMsg = errorCode.getInternalCode();
        }
        if( errorMsg != null && !errorMsg.isEmpty()) {
            message = new ResourceBundleAdaptor(PropertyInfo.ERROR_MESSAGE).getProperty(errorMsg);
            if(message != null && !message.isEmpty()){
                message = message.replace((char) 39, (char) 167);
                message = MessageFormat.format(message, (Object[]) errorParams).replace((char) 167, (char) 39);
            } else {
                message = new StringBuilder("Message not found with the code: ").append(errorMsg).toString();
            }
        }
        return message;
    }

}
