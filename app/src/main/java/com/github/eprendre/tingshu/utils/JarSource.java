package com.github.eprendre.tingshu.utils;

import androidx.annotation.Keep;
import com.github.eprendre.tingshu.sources.TingShu;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Keep
/* loaded from: E:\File2\逆向相关\正常app\听书\fe9c084e5766d61db486c228876996ab.zip\classes5.dex */
public final class JarSource {
    @NotNull
    private final String desc;
    @NotNull
    private final String name;
    @NotNull
    private final List<? extends TingShu> sourceList;

    /* JADX WARN: Multi-variable type inference failed */
    public JarSource(String name, String desc, List<? extends TingShu> sourceList) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(desc, "desc");
        Intrinsics.checkNotNullParameter(sourceList, "sourceList");
        this.name = name;
        this.desc = desc;
        this.sourceList =  sourceList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static JarSource copy$default(JarSource jarSource, String str, String str2, List list, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            str = jarSource.name;
        }
        if ((i10 & 2) != 0) {
            str2 = jarSource.desc;
        }
        if ((i10 & 4) != 0) {
            list = jarSource.sourceList;
        }
        return jarSource.copy(str, str2, list);
    }

    @NotNull
    public final String component1() {
        return this.name;
    }

    @NotNull
    public final String component2() {
        return this.desc;
    }

    @NotNull
    public final List<? extends TingShu> component3() {
        return this.sourceList;
    }

    @NotNull
    public final JarSource copy(@NotNull String name, @NotNull String desc, @NotNull List<? extends TingShu> sourceList) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(desc, "desc");
        Intrinsics.checkNotNullParameter(sourceList, "sourceList");
        return new JarSource(name, desc, sourceList);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof JarSource) {
            JarSource jarSource = (JarSource) obj;
            return Intrinsics.areEqual(this.name, jarSource.name) && Intrinsics.areEqual(this.desc, jarSource.desc) && this.sourceList.size() == jarSource.sourceList.size();
        }
        return false;
    }

    @NotNull
    public final String getDesc() {
        return this.desc;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    @NotNull
    public final List<? extends TingShu> getSourceList() {
        return this.sourceList;
    }

}