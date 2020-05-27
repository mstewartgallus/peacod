// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ast/src/main/ast.proto

package com.sstewartgallus.peacod.ast;

/**
 * <pre>
 * Fixme, just rename Def or something
 * </pre>
 * <p>
 * Protobuf type {@code peacod_ast.Def}
 */
public final class Def extends
        com.google.protobuf.GeneratedMessageV3 implements
        // @@protoc_insertion_point(message_implements:peacod_ast.Def)
        DefOrBuilder {
    public static final int TYPE_FIELD_NUMBER = 1;
    public static final int ARITY_FIELD_NUMBER = 2;
    public static final int BODY_FIELD_NUMBER = 3;
    private static final long serialVersionUID = 0L;
    // @@protoc_insertion_point(class_scope:peacod_ast.Def)
    private static final com.sstewartgallus.peacod.ast.Def DEFAULT_INSTANCE;
    private static final com.google.protobuf.Parser<Def>
            PARSER = new com.google.protobuf.AbstractParser<Def>() {
        @java.lang.Override
        public Def parsePartialFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return new Def(input, extensionRegistry);
        }
    };

    static {
        DEFAULT_INSTANCE = new com.sstewartgallus.peacod.ast.Def();
    }

    private com.sstewartgallus.peacod.ast.TypeSchemeExpr type_;
    private int arity_;
    private com.sstewartgallus.peacod.ast.Expr body_;
    private byte memoizedIsInitialized = -1;

    // Use Def.newBuilder() to construct.
    private Def(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
        super(builder);
    }

    private Def() {
    }

    private Def(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        this();
        if (extensionRegistry == null) {
            throw new java.lang.NullPointerException();
        }
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
                com.google.protobuf.UnknownFieldSet.newBuilder();
        try {
            boolean done = false;
            while (!done) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        done = true;
                        break;
                    case 10: {
                        com.sstewartgallus.peacod.ast.TypeSchemeExpr.Builder subBuilder = null;
                        if (type_ != null) {
                            subBuilder = type_.toBuilder();
                        }
                        type_ = input.readMessage(com.sstewartgallus.peacod.ast.TypeSchemeExpr.parser(), extensionRegistry);
                        if (subBuilder != null) {
                            subBuilder.mergeFrom(type_);
                            type_ = subBuilder.buildPartial();
                        }

                        break;
                    }
                    case 16: {

                        arity_ = input.readUInt32();
                        break;
                    }
                    case 26: {
                        com.sstewartgallus.peacod.ast.Expr.Builder subBuilder = null;
                        if (body_ != null) {
                            subBuilder = body_.toBuilder();
                        }
                        body_ = input.readMessage(com.sstewartgallus.peacod.ast.Expr.parser(), extensionRegistry);
                        if (subBuilder != null) {
                            subBuilder.mergeFrom(body_);
                            body_ = subBuilder.buildPartial();
                        }

                        break;
                    }
                    default: {
                        if (!parseUnknownField(
                                input, unknownFields, extensionRegistry, tag)) {
                            done = true;
                        }
                        break;
                    }
                }
            }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e) {
            throw new com.google.protobuf.InvalidProtocolBufferException(
                    e).setUnfinishedMessage(this);
        } finally {
            this.unknownFields = unknownFields.build();
            makeExtensionsImmutable();
        }
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
        return com.sstewartgallus.peacod.ast.Ast.internal_static_peacod_ast_Def_descriptor;
    }

    public static com.sstewartgallus.peacod.ast.Def parseFrom(
            java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.sstewartgallus.peacod.ast.Def parseFrom(
            java.nio.ByteBuffer data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.sstewartgallus.peacod.ast.Def parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.sstewartgallus.peacod.ast.Def parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.sstewartgallus.peacod.ast.Def parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.sstewartgallus.peacod.ast.Def parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.sstewartgallus.peacod.ast.Def parseFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static com.sstewartgallus.peacod.ast.Def parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.sstewartgallus.peacod.ast.Def parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input);
    }

    public static com.sstewartgallus.peacod.ast.Def parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.sstewartgallus.peacod.ast.Def parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static com.sstewartgallus.peacod.ast.Def parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(com.sstewartgallus.peacod.ast.Def prototype) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    public static com.sstewartgallus.peacod.ast.Def getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static com.google.protobuf.Parser<Def> parser() {
        return PARSER;
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
            UnusedPrivateParameter unused) {
        return new Def();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
        return this.unknownFields;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
    internalGetFieldAccessorTable() {
        return com.sstewartgallus.peacod.ast.Ast.internal_static_peacod_ast_Def_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        com.sstewartgallus.peacod.ast.Def.class, com.sstewartgallus.peacod.ast.Def.Builder.class);
    }

    /**
     * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
     *
     * @return Whether the type field is set.
     */
    public boolean hasType() {
        return type_ != null;
    }

    /**
     * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
     *
     * @return The type.
     */
    public com.sstewartgallus.peacod.ast.TypeSchemeExpr getType() {
        return type_ == null ? com.sstewartgallus.peacod.ast.TypeSchemeExpr.getDefaultInstance() : type_;
    }

    /**
     * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
     */
    public com.sstewartgallus.peacod.ast.TypeSchemeExprOrBuilder getTypeOrBuilder() {
        return getType();
    }

    /**
     * <pre>
     * The arity of a definition has actually pretty little to do with the type
     * </pre>
     *
     * <code>uint32 arity = 2;</code>
     *
     * @return The arity.
     */
    public int getArity() {
        return arity_;
    }

    /**
     * <code>.peacod_ast.Expr body = 3;</code>
     *
     * @return Whether the body field is set.
     */
    public boolean hasBody() {
        return body_ != null;
    }

    /**
     * <code>.peacod_ast.Expr body = 3;</code>
     *
     * @return The body.
     */
    public com.sstewartgallus.peacod.ast.Expr getBody() {
        return body_ == null ? com.sstewartgallus.peacod.ast.Expr.getDefaultInstance() : body_;
    }

    /**
     * <code>.peacod_ast.Expr body = 3;</code>
     */
    public com.sstewartgallus.peacod.ast.ExprOrBuilder getBodyOrBuilder() {
        return getBody();
    }

    @java.lang.Override
    public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized == 1) return true;
        if (isInitialized == 0) return false;

        memoizedIsInitialized = 1;
        return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
        if (type_ != null) {
            output.writeMessage(1, getType());
        }
        if (arity_ != 0) {
            output.writeUInt32(2, arity_);
        }
        if (body_ != null) {
            output.writeMessage(3, getBody());
        }
        unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
        int size = memoizedSize;
        if (size != -1) return size;

        size = 0;
        if (type_ != null) {
            size += com.google.protobuf.CodedOutputStream
                    .computeMessageSize(1, getType());
        }
        if (arity_ != 0) {
            size += com.google.protobuf.CodedOutputStream
                    .computeUInt32Size(2, arity_);
        }
        if (body_ != null) {
            size += com.google.protobuf.CodedOutputStream
                    .computeMessageSize(3, getBody());
        }
        size += unknownFields.getSerializedSize();
        memoizedSize = size;
        return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof com.sstewartgallus.peacod.ast.Def)) {
            return super.equals(obj);
        }
        com.sstewartgallus.peacod.ast.Def other = (com.sstewartgallus.peacod.ast.Def) obj;

        if (hasType() != other.hasType()) return false;
        if (hasType()) {
            if (!getType()
                    .equals(other.getType())) return false;
        }
        if (getArity()
                != other.getArity()) return false;
        if (hasBody() != other.hasBody()) return false;
        if (hasBody()) {
            if (!getBody()
                    .equals(other.getBody())) return false;
        }
        if (!unknownFields.equals(other.unknownFields)) return false;
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        if (memoizedHashCode != 0) {
            return memoizedHashCode;
        }
        int hash = 41;
        hash = (19 * hash) + getDescriptor().hashCode();
        if (hasType()) {
            hash = (37 * hash) + TYPE_FIELD_NUMBER;
            hash = (53 * hash) + getType().hashCode();
        }
        hash = (37 * hash) + ARITY_FIELD_NUMBER;
        hash = (53 * hash) + getArity();
        if (hasBody()) {
            hash = (37 * hash) + BODY_FIELD_NUMBER;
            hash = (53 * hash) + getBody().hashCode();
        }
        hash = (29 * hash) + unknownFields.hashCode();
        memoizedHashCode = hash;
        return hash;
    }

    @java.lang.Override
    public Builder newBuilderForType() {
        return newBuilder();
    }

    @java.lang.Override
    public Builder toBuilder() {
        return this == DEFAULT_INSTANCE
                ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<Def> getParserForType() {
        return PARSER;
    }

    @java.lang.Override
    public com.sstewartgallus.peacod.ast.Def getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

    /**
     * <pre>
     * Fixme, just rename Def or something
     * </pre>
     * <p>
     * Protobuf type {@code peacod_ast.Def}
     */
    public static final class Builder extends
            com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
            // @@protoc_insertion_point(builder_implements:peacod_ast.Def)
            com.sstewartgallus.peacod.ast.DefOrBuilder {
        private com.sstewartgallus.peacod.ast.TypeSchemeExpr type_;
        private com.google.protobuf.SingleFieldBuilderV3<
                com.sstewartgallus.peacod.ast.TypeSchemeExpr, com.sstewartgallus.peacod.ast.TypeSchemeExpr.Builder, com.sstewartgallus.peacod.ast.TypeSchemeExprOrBuilder> typeBuilder_;
        private int arity_;
        private com.sstewartgallus.peacod.ast.Expr body_;
        private com.google.protobuf.SingleFieldBuilderV3<
                com.sstewartgallus.peacod.ast.Expr, com.sstewartgallus.peacod.ast.Expr.Builder, com.sstewartgallus.peacod.ast.ExprOrBuilder> bodyBuilder_;

        // Construct using com.sstewartgallus.peacod.ast.Def.newBuilder()
        private Builder() {
            maybeForceBuilderInitialization();
        }

        private Builder(
                com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
            super(parent);
            maybeForceBuilderInitialization();
        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return com.sstewartgallus.peacod.ast.Ast.internal_static_peacod_ast_Def_descriptor;
        }

        @java.lang.Override
        protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return com.sstewartgallus.peacod.ast.Ast.internal_static_peacod_ast_Def_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            com.sstewartgallus.peacod.ast.Def.class, com.sstewartgallus.peacod.ast.Def.Builder.class);
        }

        private void maybeForceBuilderInitialization() {
            if (com.google.protobuf.GeneratedMessageV3
                    .alwaysUseFieldBuilders) {
            }
        }

        @java.lang.Override
        public Builder clear() {
            super.clear();
            if (typeBuilder_ == null) {
                type_ = null;
            } else {
                type_ = null;
                typeBuilder_ = null;
            }
            arity_ = 0;

            if (bodyBuilder_ == null) {
                body_ = null;
            } else {
                body_ = null;
                bodyBuilder_ = null;
            }
            return this;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
            return com.sstewartgallus.peacod.ast.Ast.internal_static_peacod_ast_Def_descriptor;
        }

        @java.lang.Override
        public com.sstewartgallus.peacod.ast.Def getDefaultInstanceForType() {
            return com.sstewartgallus.peacod.ast.Def.getDefaultInstance();
        }

        @java.lang.Override
        public com.sstewartgallus.peacod.ast.Def build() {
            com.sstewartgallus.peacod.ast.Def result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        @java.lang.Override
        public com.sstewartgallus.peacod.ast.Def buildPartial() {
            com.sstewartgallus.peacod.ast.Def result = new com.sstewartgallus.peacod.ast.Def(this);
            if (typeBuilder_ == null) {
                result.type_ = type_;
            } else {
                result.type_ = typeBuilder_.build();
            }
            result.arity_ = arity_;
            if (bodyBuilder_ == null) {
                result.body_ = body_;
            } else {
                result.body_ = bodyBuilder_.build();
            }
            onBuilt();
            return result;
        }

        @java.lang.Override
        public Builder clone() {
            return super.clone();
        }

        @java.lang.Override
        public Builder setField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                java.lang.Object value) {
            return super.setField(field, value);
        }

        @java.lang.Override
        public Builder clearField(
                com.google.protobuf.Descriptors.FieldDescriptor field) {
            return super.clearField(field);
        }

        @java.lang.Override
        public Builder clearOneof(
                com.google.protobuf.Descriptors.OneofDescriptor oneof) {
            return super.clearOneof(oneof);
        }

        @java.lang.Override
        public Builder setRepeatedField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                int index, java.lang.Object value) {
            return super.setRepeatedField(field, index, value);
        }

        @java.lang.Override
        public Builder addRepeatedField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                java.lang.Object value) {
            return super.addRepeatedField(field, value);
        }

        @java.lang.Override
        public Builder mergeFrom(com.google.protobuf.Message other) {
            if (other instanceof com.sstewartgallus.peacod.ast.Def) {
                return mergeFrom((com.sstewartgallus.peacod.ast.Def) other);
            } else {
                super.mergeFrom(other);
                return this;
            }
        }

        public Builder mergeFrom(com.sstewartgallus.peacod.ast.Def other) {
            if (other == com.sstewartgallus.peacod.ast.Def.getDefaultInstance()) return this;
            if (other.hasType()) {
                mergeType(other.getType());
            }
            if (other.getArity() != 0) {
                setArity(other.getArity());
            }
            if (other.hasBody()) {
                mergeBody(other.getBody());
            }
            this.mergeUnknownFields(other.unknownFields);
            onChanged();
            return this;
        }

        @java.lang.Override
        public final boolean isInitialized() {
            return true;
        }

        @java.lang.Override
        public Builder mergeFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            com.sstewartgallus.peacod.ast.Def parsedMessage = null;
            try {
                parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                parsedMessage = (com.sstewartgallus.peacod.ast.Def) e.getUnfinishedMessage();
                throw e.unwrapIOException();
            } finally {
                if (parsedMessage != null) {
                    mergeFrom(parsedMessage);
                }
            }
            return this;
        }

        /**
         * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
         *
         * @return Whether the type field is set.
         */
        public boolean hasType() {
            return typeBuilder_ != null || type_ != null;
        }

        /**
         * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
         *
         * @return The type.
         */
        public com.sstewartgallus.peacod.ast.TypeSchemeExpr getType() {
            if (typeBuilder_ == null) {
                return type_ == null ? com.sstewartgallus.peacod.ast.TypeSchemeExpr.getDefaultInstance() : type_;
            } else {
                return typeBuilder_.getMessage();
            }
        }

        /**
         * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
         */
        public Builder setType(com.sstewartgallus.peacod.ast.TypeSchemeExpr value) {
            if (typeBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                type_ = value;
                onChanged();
            } else {
                typeBuilder_.setMessage(value);
            }

            return this;
        }

        /**
         * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
         */
        public Builder setType(
                com.sstewartgallus.peacod.ast.TypeSchemeExpr.Builder builderForValue) {
            if (typeBuilder_ == null) {
                type_ = builderForValue.build();
                onChanged();
            } else {
                typeBuilder_.setMessage(builderForValue.build());
            }

            return this;
        }

        /**
         * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
         */
        public Builder mergeType(com.sstewartgallus.peacod.ast.TypeSchemeExpr value) {
            if (typeBuilder_ == null) {
                if (type_ != null) {
                    type_ =
                            com.sstewartgallus.peacod.ast.TypeSchemeExpr.newBuilder(type_).mergeFrom(value).buildPartial();
                } else {
                    type_ = value;
                }
                onChanged();
            } else {
                typeBuilder_.mergeFrom(value);
            }

            return this;
        }

        /**
         * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
         */
        public Builder clearType() {
            if (typeBuilder_ == null) {
                type_ = null;
                onChanged();
            } else {
                type_ = null;
                typeBuilder_ = null;
            }

            return this;
        }

        /**
         * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
         */
        public com.sstewartgallus.peacod.ast.TypeSchemeExpr.Builder getTypeBuilder() {

            onChanged();
            return getTypeFieldBuilder().getBuilder();
        }

        /**
         * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
         */
        public com.sstewartgallus.peacod.ast.TypeSchemeExprOrBuilder getTypeOrBuilder() {
            if (typeBuilder_ != null) {
                return typeBuilder_.getMessageOrBuilder();
            } else {
                return type_ == null ?
                        com.sstewartgallus.peacod.ast.TypeSchemeExpr.getDefaultInstance() : type_;
            }
        }

        /**
         * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
         */
        private com.google.protobuf.SingleFieldBuilderV3<
                com.sstewartgallus.peacod.ast.TypeSchemeExpr, com.sstewartgallus.peacod.ast.TypeSchemeExpr.Builder, com.sstewartgallus.peacod.ast.TypeSchemeExprOrBuilder>
        getTypeFieldBuilder() {
            if (typeBuilder_ == null) {
                typeBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
                        com.sstewartgallus.peacod.ast.TypeSchemeExpr, com.sstewartgallus.peacod.ast.TypeSchemeExpr.Builder, com.sstewartgallus.peacod.ast.TypeSchemeExprOrBuilder>(
                        getType(),
                        getParentForChildren(),
                        isClean());
                type_ = null;
            }
            return typeBuilder_;
        }

        /**
         * <pre>
         * The arity of a definition has actually pretty little to do with the type
         * </pre>
         *
         * <code>uint32 arity = 2;</code>
         *
         * @return The arity.
         */
        public int getArity() {
            return arity_;
        }

        /**
         * <pre>
         * The arity of a definition has actually pretty little to do with the type
         * </pre>
         *
         * <code>uint32 arity = 2;</code>
         *
         * @param value The arity to set.
         * @return This builder for chaining.
         */
        public Builder setArity(int value) {

            arity_ = value;
            onChanged();
            return this;
        }

        /**
         * <pre>
         * The arity of a definition has actually pretty little to do with the type
         * </pre>
         *
         * <code>uint32 arity = 2;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearArity() {

            arity_ = 0;
            onChanged();
            return this;
        }

        /**
         * <code>.peacod_ast.Expr body = 3;</code>
         *
         * @return Whether the body field is set.
         */
        public boolean hasBody() {
            return bodyBuilder_ != null || body_ != null;
        }

        /**
         * <code>.peacod_ast.Expr body = 3;</code>
         *
         * @return The body.
         */
        public com.sstewartgallus.peacod.ast.Expr getBody() {
            if (bodyBuilder_ == null) {
                return body_ == null ? com.sstewartgallus.peacod.ast.Expr.getDefaultInstance() : body_;
            } else {
                return bodyBuilder_.getMessage();
            }
        }

        /**
         * <code>.peacod_ast.Expr body = 3;</code>
         */
        public Builder setBody(com.sstewartgallus.peacod.ast.Expr value) {
            if (bodyBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                body_ = value;
                onChanged();
            } else {
                bodyBuilder_.setMessage(value);
            }

            return this;
        }

        /**
         * <code>.peacod_ast.Expr body = 3;</code>
         */
        public Builder setBody(
                com.sstewartgallus.peacod.ast.Expr.Builder builderForValue) {
            if (bodyBuilder_ == null) {
                body_ = builderForValue.build();
                onChanged();
            } else {
                bodyBuilder_.setMessage(builderForValue.build());
            }

            return this;
        }

        /**
         * <code>.peacod_ast.Expr body = 3;</code>
         */
        public Builder mergeBody(com.sstewartgallus.peacod.ast.Expr value) {
            if (bodyBuilder_ == null) {
                if (body_ != null) {
                    body_ =
                            com.sstewartgallus.peacod.ast.Expr.newBuilder(body_).mergeFrom(value).buildPartial();
                } else {
                    body_ = value;
                }
                onChanged();
            } else {
                bodyBuilder_.mergeFrom(value);
            }

            return this;
        }

        /**
         * <code>.peacod_ast.Expr body = 3;</code>
         */
        public Builder clearBody() {
            if (bodyBuilder_ == null) {
                body_ = null;
                onChanged();
            } else {
                body_ = null;
                bodyBuilder_ = null;
            }

            return this;
        }

        /**
         * <code>.peacod_ast.Expr body = 3;</code>
         */
        public com.sstewartgallus.peacod.ast.Expr.Builder getBodyBuilder() {

            onChanged();
            return getBodyFieldBuilder().getBuilder();
        }

        /**
         * <code>.peacod_ast.Expr body = 3;</code>
         */
        public com.sstewartgallus.peacod.ast.ExprOrBuilder getBodyOrBuilder() {
            if (bodyBuilder_ != null) {
                return bodyBuilder_.getMessageOrBuilder();
            } else {
                return body_ == null ?
                        com.sstewartgallus.peacod.ast.Expr.getDefaultInstance() : body_;
            }
        }

        /**
         * <code>.peacod_ast.Expr body = 3;</code>
         */
        private com.google.protobuf.SingleFieldBuilderV3<
                com.sstewartgallus.peacod.ast.Expr, com.sstewartgallus.peacod.ast.Expr.Builder, com.sstewartgallus.peacod.ast.ExprOrBuilder>
        getBodyFieldBuilder() {
            if (bodyBuilder_ == null) {
                bodyBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
                        com.sstewartgallus.peacod.ast.Expr, com.sstewartgallus.peacod.ast.Expr.Builder, com.sstewartgallus.peacod.ast.ExprOrBuilder>(
                        getBody(),
                        getParentForChildren(),
                        isClean());
                body_ = null;
            }
            return bodyBuilder_;
        }

        @java.lang.Override
        public final Builder setUnknownFields(
                final com.google.protobuf.UnknownFieldSet unknownFields) {
            return super.setUnknownFields(unknownFields);
        }

        @java.lang.Override
        public final Builder mergeUnknownFields(
                final com.google.protobuf.UnknownFieldSet unknownFields) {
            return super.mergeUnknownFields(unknownFields);
        }


        // @@protoc_insertion_point(builder_scope:peacod_ast.Def)
    }

}
