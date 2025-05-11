package com.daxen.mydancekmpsharedui.features.academy.classes.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.data.academy.classes.models.BaseClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.TeacherModel

@Composable
fun ClassCard(
    name: String,
    hour: String,
    teachers: List<TeacherModel>,
    status: String,
    modifier: Modifier = Modifier
) {
    val padding = LocalPadding.current
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding.small),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(padding.normal)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                ClassStatusChip(status = status)
            }
            
            Spacer(modifier = Modifier.height(padding.small))
            
            Text(
                text = "Hora: $hour",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(padding.tiny))
            
            TeachersList(teachers = teachers)
        }
    }
}

@Composable
fun ClassStatusChip(status: String) {
    val backgroundColor = when(status.lowercase()) {
        "available" -> Color(0xFFE8F5E9) // Verde claro
        "waitlist" -> Color(0xFFFFF8E1)  // Ámbar claro
        else -> Color(0xFFEFEFEF)         // Gris claro por defecto
    }
    
    val textColor = when(status.lowercase()) {
        "available" -> Color(0xFF2E7D32) // Verde oscuro
        "waitlist" -> Color(0xFFF57F17)  // Ámbar oscuro
        else -> Color(0xFF757575)         // Gris oscuro por defecto
    }
    
    val text = when(status.lowercase()) {
        "available" -> "Disponible"
        "waitlist" -> "Lista de Espera"
        else -> status
    }
    
    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun TeachersList(teachers: List<TeacherModel>) {
    Column {
        Text(
            text = "Profesor${if (teachers.size > 1) "es" else ""}:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        teachers.forEachIndexed { index, teacher ->
            if (index > 0) {
                Spacer(modifier = Modifier.height(2.dp))
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = teacher.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
} 