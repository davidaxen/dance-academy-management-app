package com.daxen.mydancekmpsharedui.features.reservation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.features.reservation.utils.DisplayClass

@Composable
internal fun DanceClassCard(
    danceClass: DisplayClass,
    onReserveClick: (DisplayClass) -> Unit,
    openBottomSheet: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = LocalPadding.current.tiny),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Box(modifier = Modifier.clickable { openBottomSheet() }) {
            Row(
                modifier = Modifier.padding(LocalPadding.current.normal),
                verticalAlignment = Alignment.CenterVertically
            ) {

    //            Image(
    //                painter = painterResource(id = danceClass.imageRes),
    //                contentDescription = null,
    //                modifier = Modifier.size(60.dp).clip(CircleShape)
    //            )
                Text(danceClass.hour, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                Column(modifier = Modifier.weight(1f).padding(horizontal = LocalPadding.current.normal)) {
                    Text(danceClass.name.uppercase(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
//                    Text("Dia: ${danceClass.origin.name}", fontSize = 14.sp, color = Color.Gray)
//                    Text(danceClass.teacherId, fontSize = 14.sp, color = Color.Gray)
                    Text("Marley & Leo", fontSize = 14.sp, color = Color.Gray)
                }

//                Button(
//                    onClick = {
//                        onReserveClick(danceClass)
//                    },
//                    enabled = danceClass.availableSpots > 0,
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = if (danceClass.availableSpots > 0) Color.Green else Color.Gray
//                    )
//                ) {
//                    Text(if (danceClass.availableSpots > 0) "Reservar" else "Llena")
//                }
            }
        }
    }
}