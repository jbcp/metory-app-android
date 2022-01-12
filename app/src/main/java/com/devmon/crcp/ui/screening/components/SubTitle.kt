package com.devmon.crcp.ui.screening.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmon.crcp.R

@Composable
fun SubTitle(title: String) {
    Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 16.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Divider(
            modifier = Modifier.width(45.dp),
            color = colorResource(id = R.color.color_blue_400),
            thickness = 3.dp,
        )
    }
}