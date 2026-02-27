import React, { useState } from 'react'
import Box from '@mui/material/Box';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import { ActivityTypeArray, ActivityTypeLabels } from '../constants/ActivityType';
import { TextField } from '@mui/material';
import Button from '@mui/material/Button';
import { addActivity } from '../services/api';

const ActivityForm = (onActivitiesAdded) => {
  const [activity, setActivity] = useState({ 
    type: 'RUNNING', 
    duration:'',
    caloriesBurned:'',
    additionalMetrics:{},
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {

    // Parse line-separated key:value format into JSON object
    const parseMetrics = (input) => {
      if (!input || typeof input !== 'string') return {};
      const lines = input.trim().split('\n');
      const result = {};
      lines.forEach(line => {
        const [key, ...valueParts] = line.split(':');
        if (key && valueParts.length > 0) {
          let value = valueParts.join(':').trim();
          // Try to parse number, otherwise keep as string
          result[key.trim()] = isNaN(value) ? value : Number(value);
        }
      });
      return result;
    };

    const parsedMetrics = parseMetrics(activity.additionalMetrics);

    const activityToSend = {
      ...activity,
      additionalMetrics: parsedMetrics
    };
      await addActivity(activity);

    
       onActivitiesAdded();
        setActivity( {
           type: 'RUNNING', 
            duration:'',
            caloriesBurned:'',
            additionalMetrics:{},
          })
         } catch (error) {
            console.error('Error:', error);
       }
  };

  return (
    <div>
        <Box component="form" 
        onSubmit={handleSubmit}
        sx={{mb: 2, p: 2, background: '#424242ff', 
        borderRadius: '5px', color:'#ecfaffff'}}>
            <h3>Activity Form</h3>

  <FormControl fullWidth
        sx={{mb: 2, background: '#424242ff', 
        borderRadius: '5px', color:'#ecfaffff !important'}}>
       
        <InputLabel
           sx={{mb: 2, p: 2, background: '#424242ff', 
        borderRadius: '5px', color:'#ecfaffff'}}
        >Activity Type</InputLabel>
       
        <Select sx={{ mb: 2,
          p: 2, background: '#424242ff', 
        borderRadius: '5px', color:'#ecfaffff'
        }}
          value={activity.type}
          onChange={(e) => setActivity({
            ...activity, 
            type: e.target.value
            })}
          label="Activity Type">
          {ActivityTypeArray.map((type) => (
            <MenuItem key={type} value={type}>
              {ActivityTypeLabels[type]}
            </MenuItem>
          ))}
          </Select>
      </FormControl>
     
      <TextField fullWidth label="Duration (minutes)"
              sx={{mb: 2, background: '#424242ff', 
        borderRadius: '5px',
        '& .MuiInputBase-input': { color: '#ecfaffff' },
        '& .MuiInputLabel-root': { color: '#ecfaffff' },
        '& .MuiOutlinedInput-root': {
          '& fieldset': { borderColor: '#ecfaffff' },
          '&:hover fieldset': { borderColor: '#ecfaffff' },
          '&.Mui-focused fieldset': { borderColor: '#ecfaffff' }
        }}}
        value={activity.duration}
        onChange={(e) => setActivity({
          ...activity, 
          duration: e.target.value
          })}
           />
      <TextField fullWidth label="Calories Burned"
        value={activity.caloriesBurned}
        onChange={(e) => setActivity({
          ...activity,
          caloriesBurned: e.target.value      
          })}
        sx={{mb: 2, background: '#424242ff', 
        borderRadius: '5px',
        '& .MuiInputBase-input': { color: '#ecfaffff' },
        '& .MuiInputLabel-root': { color: '#ecfaffff' },
        '& .MuiOutlinedInput-root': {
          '& fieldset': { borderColor: '#ecfaffff' },
          '&:hover fieldset': { borderColor: '#ecfaffff' },
          '&.Mui-focused fieldset': { borderColor: '#ecfaffff' }
        }}} />

        {/* <TextField fullWidth label="Additional Metrics (JSON format)"
              value={typeof activity.additionalMetrics === 'object' 
        ? Object.entries(activity.additionalMetrics)
            .map(([key, val]) => `${key}: ${val}`)
            .join('\n')
        : activity.additionalMetrics}
          onChange={(e) => setActivity({
            ...activity,
            additionalMetrics: e.target.value      
          })}
        sx={{mb: 2, background: '#424242ff', 
        borderRadius: '5px',
        '& .MuiInputBase-input': { color: '#ecfaffff' },
        '& .MuiInputLabel-root': { color: '#ecfaffff' },
        '& .MuiOutlinedInput-root': {
          '& fieldset': { borderColor: '#ecfaffff' },
          '&:hover fieldset': { borderColor: '#ecfaffff' },
          '&.Mui-focused fieldset': { borderColor: '#ecfaffff' }
        }}} /> */}
     

            <Button type='submit' variant="contained" sx={{ mt: 2 }}>Add Activity</Button>

    </Box>
    </div>
  )
}

export default ActivityForm