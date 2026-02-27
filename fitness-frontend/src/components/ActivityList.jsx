import React, { useEffect } from 'react'
import { useNavigate } from 'react-router';
import { Card, Typography } from '@mui/material';
import { getActivities } from '../services/api';
import Grid from '@mui/material/Grid';
import CardContent from '@mui/material/CardContent';

const ActivityList = () => {

    const [activities, setActivities] = React.useState([]);
    const navigate = useNavigate();

const fetchActivities = async () => {
            try {
                const response = await getActivities();
                setActivities(response.data);
            }catch(error){
                        console.error(error);
                    }
                };
        

    useEffect(() => {
            fetchActivities();
        }, []);


  return (
        <Grid container rowSpacing={1} 
        spacing ={{xs:2, md:3}}
        columns={{xs:4, sm:8,md:12}}
        columnSpacing={{ xs: 1, sm: 2, md: 3 }}
        
                sx={{mb: 2, background: '#424242ff', 
        borderRadius: '5px', color:'#ecfaffff !important'}}>
            {activities.map(activity => (
                <Grid key={activity.id} xs={12} sm={6} md={4}
                        sx={{mb: 2, background: '#424242ff', 
        borderRadius: '5px', color:'#ecfaffff !important'}}
                >
                    <Card         
                    sx={{mb: 2, background: '#424242ff',
                    borderRadius: '5px', color:'#ecfaffff !important'}}
                    
                    onClick={() => navigate(`/activities/${activity.id}`)}

                    >
                        <CardContent
                                sx={{mb: 2, background: '#424242ff', cursor:'pointer',
                                borderRadius: '5px', color:'#ecfaffff !important'}}>
                            <Typography variant='h'>{activity.type}</Typography>
                            <Typography variant='body2'>Duration: {activity.duration}</Typography>
                            <Typography>Calories: {activity.caloriesBurned}</Typography>
                            </CardContent>
                    </Card>
                </Grid>
            ))}
        </Grid>
  )
}

export default ActivityList