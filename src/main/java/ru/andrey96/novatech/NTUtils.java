package ru.andrey96.novatech;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class NTUtils {
	
	public static MovingObjectPosition rayTrace(Entity entity, double distance) {
        MovingObjectPosition objectMouseOver = entity.rayTrace(distance, 1f);
        double d1 = distance;
        Vec3 vec3 = entity.getPositionEyes(1f);
        
        if (objectMouseOver != null)
            d1 = objectMouseOver.hitVec.distanceTo(vec3);
        
        Vec3 vec31 = entity.getLook(1f);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
        Entity pointedEntity = null;
        Vec3 vec33 = null;
        float f1 = 1.0F;
        List list = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance).expand((double)f1, (double)f1, (double)f1));
        double d2 = d1;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity1 = (Entity)list.get(i);
            if (entity1.canBeCollidedWith())
            {
                float f2 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f2, (double)f2, (double)f2);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3))
                {
                    if (0.0D < d2 || d2 == 0.0D)
                    {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                }
                else if (movingobjectposition != null)
                {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D)
                    {
                        if (entity1 == entity.ridingEntity && !entity.canRiderInteract())
                        {
                            if (d2 == 0.0D)
                            {
                                pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                            }
                        }
                        else
                        {
                            pointedEntity = entity1;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }
        }

        if (pointedEntity != null && (d2 < d1 || objectMouseOver == null))
            return new MovingObjectPosition(pointedEntity, vec33);
        return objectMouseOver;
	}
	
}
