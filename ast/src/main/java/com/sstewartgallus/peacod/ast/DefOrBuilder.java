// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ast/src/main/ast.proto

package com.sstewartgallus.peacod.ast;

public interface DefOrBuilder extends
        // @@protoc_insertion_point(interface_extends:peacod_ast.Def)
        com.google.protobuf.MessageOrBuilder {

    /**
     * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
     *
     * @return Whether the type field is set.
     */
    boolean hasType();

    /**
     * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
     *
     * @return The type.
     */
    com.sstewartgallus.peacod.ast.TypeSchemeExpr getType();

    /**
     * <code>.peacod_ast.TypeSchemeExpr type = 1;</code>
     */
    com.sstewartgallus.peacod.ast.TypeSchemeExprOrBuilder getTypeOrBuilder();

    /**
     * <pre>
     * The arity of a definition has actually pretty little to do with the type
     * </pre>
     *
     * <code>uint32 arity = 2;</code>
     *
     * @return The arity.
     */
    int getArity();

    /**
     * <code>.peacod_ast.Expr body = 3;</code>
     *
     * @return Whether the body field is set.
     */
    boolean hasBody();

    /**
     * <code>.peacod_ast.Expr body = 3;</code>
     *
     * @return The body.
     */
    com.sstewartgallus.peacod.ast.Expr getBody();

    /**
     * <code>.peacod_ast.Expr body = 3;</code>
     */
    com.sstewartgallus.peacod.ast.ExprOrBuilder getBodyOrBuilder();
}
