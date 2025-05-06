package com.sryang.library

fun rejectedPermissions(permissionResultMap : Map<String, Boolean>) : Set<String> {
    val rejectedPermissions /* 거부한 권한 */ = permissionResultMap.filterValues { !it }.keys
    return rejectedPermissions
}

fun rejectedPermissionsString(requiredPermissions : List<String>, permissionResultMap : Map<String, Boolean>) : String{
    val rejectedPermissions /* 거부한 권한 */ = rejectedPermissions(permissionResultMap)
    return if (rejectedPermissions.none { it in requiredPermissions }) { "" } else { "${rejectedPermissions.joinToString()} required for the sample" }
}