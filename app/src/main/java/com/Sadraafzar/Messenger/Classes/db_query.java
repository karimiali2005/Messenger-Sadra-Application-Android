package com.Sadraafzar.Messenger.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.Sadraafzar.Messenger.Model.Changetype;
import com.Sadraafzar.Messenger.Model.mesCompanyChange_ResultDTO;
import com.Sadraafzar.Messenger.Model.mesMessageChange_ResultDTO;
import com.Sadraafzar.Messenger.Model.mesStatusMessageChange_ResultDTO;
import com.Sadraafzar.Messenger.Model.mesTypeMessageChange_ResultDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class db_query {

    public mesCompanyChange_ResultDTO select_table_mesCompany(dbConnector db,int idcompaney)
    {
        String query = "SELECT * FROM mesCompany WHERE pkfCompany="+idcompaney;
        Cursor c = null;
        c = db.select(query);
        mesCompanyChange_ResultDTO mc=new mesCompanyChange_ResultDTO();
        if(c!=null&&c.moveToFirst())
        {
            mc.companyName=c.getString(c.getColumnIndex("companyName"));
            mc.CountAll=c.getInt(c.getColumnIndex("CountAll"));
            mc.CountRecive=c.getInt(c.getColumnIndex("CountRecive"));
            mc.CountSend=c.getInt(c.getColumnIndex("CountSend"));
            mc.CountShow=c.getInt(c.getColumnIndex("CountShow"));
            mc.number=c.getString(c.getColumnIndex("number"));
            mc.pkfCompany=c.getInt(c.getColumnIndex("pkfCompany"));

        }
        return mc;

    }
    public void update_table_mesCompany(dbConnector db, mesCompanyChange_ResultDTO m)
    {
        Boolean status = true;
        ContentValues values = new ContentValues();
        values.put("pkfCompany", m.pkfCompany);
        values.put("companyName", m.companyName);
        values.put("number", m.number);
        values.put("CountAll", m.CountAll);
        values.put("CountSend", m.CountSend);
        values.put("CountRecive", m.CountRecive);
        values.put("CountShow", m.CountShow);

        status = db.update("mesCompany", values, "pkfCompany=" + m.pkfCompany);
    }
    public void insert_table_mesCompany(dbConnector db, mesCompanyChange_ResultDTO m)
    {
        Boolean status = true;
        ContentValues values = new ContentValues();
        values.put("pkfCompany", m.pkfCompany);
        values.put("companyName", m.companyName);
        values.put("number", m.number);
        values.put("CountAll", m.CountAll);
        values.put("CountSend", m.CountSend);
        values.put("CountRecive", m.CountRecive);
        values.put("CountShow ", m.CountShow);

        status = db.insert("mesCompany", values);
    }

    //==================================================================================================
    public int select_cunt_dont_read(Context context,int idcompaney)
    {
        dbConnector db = new dbConnector(context, app.Database.dbName, null, 1);
        int idstatus=getstutosmessage(db,4);
        String query = "SELECT * FROM mesMessage WHERE FkfCompanyId="+idcompaney+" and FkfStatusMessage !="+idstatus;
        Cursor c = null;
        c = db.select(query);
        int i=0;
        if(c!=null&&c.moveToFirst()) {

            boolean isenter = false;

            do {
i++;
            } while (c.moveToNext());
        }
        return i;
    }
    public mesMessageChange_ResultDTO select_lasted_message(Context context,int idcompaney)
    {
        dbConnector db = new dbConnector(context, app.Database.dbName, null, 1);
        String query = "SELECT * FROM mesMessage WHERE FkfCompanyId="+idcompaney+" ORDER BY pkfMessage DESC LIMIT 1";
        Cursor c = null;
        c = db.select(query);
        mesMessageChange_ResultDTO m=new mesMessageChange_ResultDTO();
        if(c!=null&&c.moveToFirst()) {
        //  String date2 = new Date(0).toString();
            String date2= "Tue Apr 21 02:20:00 GMT+04:30 2020";
            m.changeDateEN=new Date((!c.getString(c.getColumnIndex("changeDateEN")).equals(""))?c.getString(c.getColumnIndex("changeDateEN")):date2);
            m.FkfCompanyId=c.getInt(c.getColumnIndex("FkfCompanyId"));
            m.FkfDocument=c.getInt(c.getColumnIndex("FkfDocument"));
            m.FkfSource=c.getInt(c.getColumnIndex("FkfSource"));
            m.FkfStatusMessage=c.getInt(c.getColumnIndex("FkfStatusMessage"));
            m.FkfTypeMessage=c.getInt(c.getColumnIndex("FkfTypeMessage"));
            m.FkfUser=c.getInt(c.getColumnIndex("FkfUser"));
            m.isDelete= (c.getInt(c.getColumnIndex("isDelete"))) == 1;
            m.message=c.getString(c.getColumnIndex("message"));
            m.pkfMessage=c.getInt(c.getColumnIndex("pkfMessage"));
            m.ansswerDate= c.getString(c.getColumnIndex("ansswerDate"));
            m.ansswerDateEN=new Date((!c.getString(c.getColumnIndex("ansswerDateEN")).equals(""))?c.getString(c.getColumnIndex("ansswerDateEN")):date2);
            m.ansswerTime= c.getString(c.getColumnIndex("ansswerTime"));
            m.reciveDate= c.getString(c.getColumnIndex("reciveDate"));
            m.reciveDateEN=new Date((!c.getString(c.getColumnIndex("reciveDateEN")).equals(""))?c.getString(c.getColumnIndex("reciveDateEN")):date2);
            m.reciveTime= c.getString(c.getColumnIndex("reciveTime"));
            m.replay=c.getInt(c.getColumnIndex("replay"));
            m.replayDiscript= c.getString(c.getColumnIndex("replayDiscript"));
            m.sendDate= c.getString(c.getColumnIndex("sendDate"));
            m.sendDateEN=new Date((!c.getString(c.getColumnIndex("sendDateEN")).equals(""))?c.getString(c.getColumnIndex("sendDateEN")):date2);
            m.sendTime= c.getString(c.getColumnIndex("sendTime"));
            m.showDate= c.getString(c.getColumnIndex("showDate"));
            m.showDateEN=new Date((!c.getString(c.getColumnIndex("showDateEN")).equals(""))?c.getString(c.getColumnIndex("showDateEN")):date2);
            m.showTime= c.getString(c.getColumnIndex("showTime"));

        }

        return m;

    }
    public void update_table_mesMessage(dbConnector db, mesMessageChange_ResultDTO m)
    {
        try{
            Boolean status = true;
            ContentValues values = new ContentValues();
            values.put("pkfMessage", m.pkfMessage);
            values.put("FkfUser",m.FkfUser);
            values.put("message", m.message);
            values.put("FkfStatusMessage", m.FkfStatusMessage);
            values.put("FkfTypeMessage", m.FkfTypeMessage );
            values.put("FkfSource", m.FkfSource);
            values.put("FkfCompanyId", m.FkfCompanyId);

            values.put("sendDate", m.sendDate);
            values.put("reciveDate", m.reciveDate);
            values.put("showDate", m.showDate);
            values.put("ansswerDate", m.message);
            values.put("isDelete",(m.isDelete)?1:0);

            values.put("FkfDocument", m.FkfDocument);
            values.put("replay", m.replay);

            values.put("sendTime", m.sendTime);
            values.put("reciveTime", m.reciveTime);
            values.put("showTime", m.showTime);
            values.put("ansswerTime", m.ansswerTime);
            values.put("sendDateEN",(m.sendDate==null)? "": m.sendDate);

            values.put("reciveDateEN",(m.reciveDateEN==null)? "": m.reciveDateEN.toString());
            values.put("showDateEN",(m.showDateEN==null)? "": m.showDateEN.toString());
            values.put("ansswerDateEN",(m.ansswerDateEN==null)? "": m.ansswerDateEN.toString());
            values.put("replayDiscript", m.replayDiscript);
            values.put("changeDateEN",(m.changeDateEN==null)? "": m.changeDateEN.toString());

            status = db.update("mesMessage", values, " pkfMessage=" + m.pkfMessage);
        }catch (Exception ex)
        {
String s=ex.getMessage();
        }

    }
    public void insert_table_mesMessage(dbConnector db, mesMessageChange_ResultDTO m)
    {
        Boolean status = true;
        ContentValues values = new ContentValues();
        values.put("pkfMessage", m.pkfMessage);
        values.put("FkfUser",m.FkfUser);
        values.put("message", m.message);
        values.put("FkfStatusMessage", m.FkfStatusMessage);
        values.put("FkfTypeMessage", m.FkfTypeMessage );
        values.put("FkfSource", m.FkfSource);
        values.put("FkfCompanyId", m.FkfCompanyId);

        values.put("sendDate", m.sendDate);
        values.put("reciveDate", m.reciveDate);
        values.put("showDate", m.showDate);
        values.put("ansswerDate", m.message);
        values.put("isDelete",(m.isDelete)?1:0);

        values.put("FkfDocument", m.FkfDocument);
        values.put("replay", m.replay);

        values.put("sendTime", m.sendTime);
        values.put("reciveTime", m.reciveTime);
        values.put("showTime", m.showTime);
        values.put("ansswerTime", m.ansswerTime);
        values.put("sendDateEN",(m.sendDate==null)? "": m.sendDate);

        values.put("reciveDateEN",(m.reciveDateEN==null)? "": m.reciveDateEN.toString());
        values.put("showDateEN",(m.showDateEN==null)? "": m.showDateEN.toString());
        values.put("ansswerDateEN",(m.ansswerDateEN==null)? "": m.ansswerDateEN.toString());
        values.put("replayDiscript", m.replayDiscript);
        values.put("changeDateEN",(m.changeDateEN==null)? "": m.changeDateEN.toString());


        status = db.insert("mesMessage", values);
    }
    public void del_table_mesMessage(dbConnector db, mesMessageChange_ResultDTO m)
    {
        db.Delete("mesMessage","pkfMessage="+String.valueOf(m.pkfMessage));
    }
    //===================================================================================================
    public void update_table_mesStatus(dbConnector db, mesStatusMessageChange_ResultDTO m)
    {
        ContentValues values = new ContentValues();
        Boolean status = true;

        values.put("pkfStatusMessage ", m.pkfStatusMessage);
        values.put("statusMessage", m.statusMessage);
        values.put("statusMessageCode", m.statusMessageCode);
        values.put("memo", m.memo);
        values.put("changeDateEN", m.changeDateEN.toString());


        status = db.update("mesStatus", values, " pkfStatusMessage=" + m.pkfStatusMessage);
    }
    public void insert_table_mesStatus(dbConnector db, mesStatusMessageChange_ResultDTO m)
    {
        ContentValues values = new ContentValues();
        Boolean status = true;

        values.put("pkfStatusMessage ", m.pkfStatusMessage);
        values.put("statusMessage", (m.statusMessage));
        values.put("statusMessageCode", (m.statusMessageCode));
        values.put("memo", (m.memo));
        values.put("changeDateEN", (m.changeDateEN.toString()));
        status = db.insert("mesStatus", values);
    }
    public List<mesStatusMessageChange_ResultDTO> select_table_mesStatus(dbConnector db)
    {
        String query = "SELECT * FROM mesStatus ";
        Cursor c = null;
        c = db.select(query);
        List<mesStatusMessageChange_ResultDTO> mm=new ArrayList<>();
        if(c!=null&&c.moveToFirst())
        {

            do {

                mesStatusMessageChange_ResultDTO m=new mesStatusMessageChange_ResultDTO();
                // m.companyName= c.getString(c.getColumnIndex("companyName"));
                m.changeDateEN=new Date(c.getString(c.getColumnIndex("changeDateEN")));
                m.memo=c.getString(c.getColumnIndex("memo"));
                m.pkfStatusMessage=c.getInt(c.getColumnIndex("pkfStatusMessage"));
                m.statusMessage=c.getString(c.getColumnIndex("statusMessage"));
                m.statusMessageCode=c.getString(c.getColumnIndex("statusMessageCode"));


                    mm.add(m);


            }while (c.moveToNext());

        }

        return mm;
    }
    public int getstutosmessage(dbConnector db,int stutose)
    {
        List<mesStatusMessageChange_ResultDTO>ms=select_table_mesStatus(db);
        int idstatus=0;
        for(mesStatusMessageChange_ResultDTO s:ms)
        {
            if(s.statusMessageCode.equals(String.valueOf(stutose)))
            {
                idstatus=s.pkfStatusMessage;
            }
        }
        return idstatus;
    }
    //===================================================================================================
    public void update_table_mesType(dbConnector db, mesTypeMessageChange_ResultDTO m)
    {
        Boolean status = true;
        ContentValues values = new ContentValues();
        values.put("pkfTypeMessage", m.pkfTypeMessage);
        values.put("typeMessage", (m.typeMessage));
        values.put("typeMessageCode", (m.typeMessageCode));
        values.put("memo", (m.memo));
        values.put("changeDateEN", (m.changeDateEN.toString()));

        status = db.update("mesType", values, "pkfTypeMessage=" + m.pkfTypeMessage);
    }
    public void insert_table_mesType(dbConnector db, mesTypeMessageChange_ResultDTO m)
    {
        Boolean status = true;
        ContentValues values = new ContentValues();
        values.put("pkfTypeMessage", m.pkfTypeMessage);
        values.put("typeMessage", (m.typeMessage));
        values.put("typeMessageCode", (m.typeMessageCode));
        values.put("memo", (m.memo));
        values.put("changeDateEN", (m.changeDateEN.toString()));
        status = db.insert("mesType", values);
    }

    //===================================================================================================
    public void update_table_ChangeDate(dbConnector db, String m)
    {

        ContentValues values = new ContentValues();
        Boolean status = true;
        values.put("id", 1);
        values.put("ChangeDate",m);

        status = db.update("ChangeDate", values, "id=" + 1);
    }
    public void insert_table_ChangeDate(dbConnector db, String m)
    {

        ContentValues values = new ContentValues();
        Boolean status = true;
        values.put("id", 1);
        values.put("ChangeDate",m);


        status = db.insert("ChangeDate", values);
    }

    //====================================================================================================
    public List<Changetype> selectChangetype(Context context)
    {try
    {
        dbConnector db = new dbConnector(context, app.Database.dbName, null, 1);
        String query = "SELECT * FROM Changetype";
        Cursor c = null;
        c = db.select(query);
        List<Changetype> mm=new ArrayList<>();
        if(c!=null&&c.moveToFirst())
        {

            do {

                Changetype m=new Changetype();
                m.idmessage=c.getInt(c.getColumnIndex("idmessage"));
                m.idtype=c.getInt(c.getColumnIndex("type"));



                mm.add(m);


            }while (c.moveToNext());

        }

        return mm;
    }catch (Exception e)
    {
        app.Info.ischeang=true;
    }
       return null;
    }
public void del_Changetype(Context context,int id)
{
    dbConnector db = new dbConnector(context, app.Database.dbName, null, 1);
    db.Delete("Changetype","idmessage="+id);

}

    //====================================================================================================
    public void setchengecompany(dbConnector db,int idcompaney)
    {
        mesCompanyChange_ResultDTO mc=select_table_mesCompany(db,idcompaney);
        mc.CountShow=0;
        update_table_mesCompany(db,mc);

    }
    public void setchengeviewtype(dbConnector db,mesMessageChange_ResultDTO mm)
    {
        int idstatus=getstutosmessage(db,4);
       update_table_mesMessage(db,mm);
        ContentValues values = new ContentValues();
        Boolean status = true;
        values.put("idmessage", mm.pkfMessage);
        if(mm.FkfStatusMessage==idstatus)
        {
            values.put("type",mm.FkfStatusMessage);
        }else
        {
            values.put("type",1);
        }

        status = db.insert("Changetype", values);
        app.Info.ischeang=true;
    }

}
