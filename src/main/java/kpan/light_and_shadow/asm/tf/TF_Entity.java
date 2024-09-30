package kpan.light_and_shadow.asm.tf;

import kpan.light_and_shadow.asm.core.AsmNameRemapper;
import kpan.light_and_shadow.asm.core.AsmTypes;
import kpan.light_and_shadow.asm.core.AsmUtil;
import kpan.light_and_shadow.asm.core.adapters.MyClassVisitor;
import kpan.light_and_shadow.asm.core.adapters.RedirectInvokeAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class TF_Entity {

    private static final String TARGET = "net.minecraft.entity.Entity";
    private static final String HOOK = AsmTypes.HOOK + "HK_" + "Entity";

    public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
        if (!TARGET.equals(className))
            return cv;
        ClassVisitor newcv = new MyClassVisitor(cv, className) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                String mcpName = AsmNameRemapper.runtime2McpMethodName(name);
                if (mcpName.equals("rayTrace")) {
                    mv = RedirectInvokeAdapter.virtual(mv, mcpName, HOOK, AsmTypes.WORLD, "rayTraceBlocks", AsmUtil.toMethodDesc("net.minecraft.util.math.RayTraceResult", "net.minecraft.util.math.Vec3d", "net.minecraft.util.math.Vec3d", AsmTypes.BOOL, AsmTypes.BOOL, AsmTypes.BOOL))
                            .appendCaller(TARGET)
                    ;
                    success();
                }
                return mv;
            }
        };
        return newcv;
    }
}
