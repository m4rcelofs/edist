package com.erep.dist.data;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchGroups;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProperty;
import org.directwebremoting.convert.BeanConverter;

import com.erep.dist.data.enums.RequestStatus;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.RocketQuality;
import com.erep.dist.erep.eRepTimeCalculator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject(converter = BeanConverter.class, params = @Param(name = "javascript", value = "Request"))
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroups({
		@FetchGroup(name = "reportListing", members = {
				@Persistent(name = "stats"), @Persistent(name = "group") }),
		@FetchGroup(name = "managementAccess", members = {
				@Persistent(name = "distributor"), @Persistent(name = "ip"),
				@Persistent(name = "XAppEngineCountry"),
				@Persistent(name = "XAppEngineRegion"),
				@Persistent(name = "XAppEngineCity") }) })
public class Request implements HasEncodedKey {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key					key;

	@NotPersistent
	@RemoteProperty
	private String				encodedKey;

	@Persistent
	@RemoteProperty
	private Integer				eRepDay;

	@Persistent
	@RemoteProperty
	private RequestStatus		status;

	@Persistent
	@RemoteProperty
	private RequestType			type;

	// if RequestType == rocket only
	@Persistent
	@RemoteProperty
	private RocketQuality		rocketQuality;

	// if RequestType != rocket
	@Persistent
	@RemoteProperty
	private Integer				requestedFF;

	@Persistent
	@RemoteProperty
	private Date				requestedAt;

	@Persistent
	@RemoteProperty
	private Date				completedAt;

	@Persistent
	private List<ResourceDist>	resources;

	@Unowned
	@Persistent
	@RemoteProperty
	private Distributor			distributor;

	@Persistent
	private Player				player;

	// v0.5
	@Deprecated
	@NotPersistent
	private Player				playerInfo;

	@Unowned
	@Persistent
	@RemoteProperty
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private DailyStats			stats;

	@Persistent(defaultFetchGroup = "false")
	@RemoteProperty
	private String				ip;

	@Persistent(defaultFetchGroup = "false")
	@RemoteProperty
	private String				XAppEngineCountry;

	@Persistent(defaultFetchGroup = "false")
	@RemoteProperty
	private String				XAppEngineRegion;

	@Persistent(defaultFetchGroup = "false")
	@RemoteProperty
	private String				XAppEngineCity;

	@Unowned
	@Persistent
	@RemoteProperty
	private PlayerGroup			group;

	public Request() {}

	public Request(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Integer geteRepDay() {
		return eRepDay;
	}

	public void seteRepDay(Integer eRepDay) {
		this.eRepDay = eRepDay;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public Date getRequestedAt() {
		return requestedAt;
	}

	public void setRequestedAt(Date requestedAt) {
		this.requestedAt = requestedAt;
	}

	public Date getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}

	public RequestType getType() {
		return type;
	}

	public void setType(RequestType type) {
		this.type = type;
	}

	public RocketQuality getRocketQuality() {
		return rocketQuality;
	}

	public void setRocketQuality(RocketQuality quality) {
		this.rocketQuality = quality;
	}

	public Integer getRequestedFF() {
		return requestedFF;
	}

	public void setRequestedFF(Integer requestedFF) {
		this.requestedFF = requestedFF;
	}

	public Distributor getDistributor() {
		return distributor;
	}

	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}

	public List<ResourceDist> getResources() {
		return resources;
	}

	public void setResources(List<ResourceDist> resources) {
		this.resources = resources;
	}

	@Override
	public String getEncodedKey() {
		if (key != null)
			return KeyFactory.keyToString(key);
		return encodedKey;
	}

	@Override
	public void setEncodedKey(String id) {
		if (id != null)
			this.key = KeyFactory.stringToKey(id);
		this.encodedKey = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getXAppEngineCountry() {
		return XAppEngineCountry;
	}

	public void setXAppEngineCountry(String x_AppEngine_Country) {
		XAppEngineCountry = x_AppEngine_Country;
	}

	public String getXAppEngineRegion() {
		return XAppEngineRegion;
	}

	public void setXAppEngineRegion(String x_AppEngine_Region) {
		XAppEngineRegion = x_AppEngine_Region;
	}

	public String getXAppEngineCity() {
		return XAppEngineCity;
	}

	public void setXAppEngineCity(String x_AppEngine_City) {
		XAppEngineCity = x_AppEngine_City;
	}

	public PlayerGroup getGroup() {
		return group;
	}

	public void setGroup(PlayerGroup group) {
		this.group = group;
	}

	public DailyStats getStats() {
		return stats;
	}

	public void setStats(DailyStats stats) {
		this.stats = stats;
	}

	public Date getDate() {
		return eRepTimeCalculator.toRLDate(eRepDay).toDate();
	}

	@Deprecated
	public void setDate() {
		// DWR filler only
	}

}
